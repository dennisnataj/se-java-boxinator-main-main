package se.experis.boxinator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import se.experis.boxinator.models.CommonResponse;
import se.experis.boxinator.models.dbo.*;
import se.experis.boxinator.models.dto.ShipmentContext;
import se.experis.boxinator.models.dto.ShipmentDTO;
import se.experis.boxinator.models.dto.ShipmentUpdate;
import se.experis.boxinator.repositories.*;
import se.experis.boxinator.services.AccountService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Shipments")
@CrossOrigin(origins = "${server.cors.application_origin}")
@RequestMapping("/api/${app.version}/shipments")
public class ShipmentController {
    private final AccountService accountService;

    private final ShipmentRepository shipmentRepository;
    private final AccountRepository accountRepository;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final StatusRepository statusRepository;
    private final CountryRepository countryRepository;

    public ShipmentController(AccountService accountService, ShipmentRepository shipmentRepository, AccountRepository accountRepository, ShipmentStatusRepository shipmentStatusRepository, StatusRepository statusRepository, CountryRepository countryRepository) {
        this.accountService = accountService;
        this.shipmentRepository = shipmentRepository;
        this.accountRepository = accountRepository;
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.statusRepository = statusRepository;
        this.countryRepository = countryRepository;
    }


    @PostMapping
    @Operation(summary = "Create a new shipment. Everyone can access.")
    public ResponseEntity<CommonResponse<ShipmentDTO>> createShipment(@AuthenticationPrincipal Jwt principal, @RequestBody ShipmentContext shipmentContext) {
        Account savedAccount;
        // get guest account if guest exists
        if(principal != null) {
            savedAccount = accountService.getAccountWithJwt(principal);
        } else if(shipmentContext.getGuest() != null) {
                savedAccount = accountService.getGuestAccount(shipmentContext.getGuest());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new CommonResponse<>(400,"Bad input"));
        }

        // setup new shipment
        Shipment shipment = new Shipment();
        shipment.setReceiverName(shipmentContext.getShipment().getReceiverName());
        shipment.setWeightOption(shipmentContext.getShipment().getWeightOption());
        shipment.setBoxColor(shipmentContext.getShipment().getBoxColor());

        // find country, if doesn't exist return bad request
        Optional<Country> country = countryRepository.findByName(shipmentContext.getShipment().getDestinationCountry());
        if(!country.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new CommonResponse<>(400, "Bad input"));
        }

        shipment.setDestinationCountry(country.get());
        //setting shipment calculation based on country multiplier
        shipment.setCost(200 + (shipment.getWeightOption() * country.get().getMultiplier()));

        shipment.setSender(savedAccount);
        Shipment savedShipment = shipmentRepository.save(shipment);


        Optional<Status> status = statusRepository.findById(1L);

        ShipmentStatus shipmentStatus = new ShipmentStatus(savedShipment, status.get());
        //save shipment-status
        shipmentStatus = shipmentStatusRepository.save(shipmentStatus);
        //add shipments status to the saved-shipment
        savedShipment.getShipmentStatuses().add(shipmentStatus);

        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(new ShipmentDTO(savedShipment)));
    }
    //get shipment by id endpoint
    @GetMapping("{id}")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Get shipment by id. Only registered users can access.")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<Shipment>> getShipmentById(@PathVariable Long id, @AuthenticationPrincipal Jwt principal) {
        Optional<Shipment> shipment = shipmentRepository.findById(id);
        if(shipment.isPresent()) {
            // check if user is admin or if shipment is created by account
            if(!(principal.getClaimAsStringList("roles").contains("Administrator")|| (shipment.get().getSender().getKcSubjectId() != null && shipment.get().getSender().getKcSubjectId().equals(principal.getClaimAsString("sub"))))){
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new CommonResponse<>(403, "Not allowed"));
            }
            return ResponseEntity
                    .ok()
                    .body(new CommonResponse<>(shipment.get()));
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CommonResponse<>(404, "Shipment doesn't exist"));
    }

    //User can only cancel shipment, admin can make any changes to a shipment
    @PutMapping("{id}")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Update shipment by id. Only registered users can access")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<ShipmentDTO>> updateShipment(@PathVariable Long id, @AuthenticationPrincipal Jwt principal, @RequestBody ShipmentUpdate shipmentUpdate) {
        Shipment returnShipment = new Shipment();

        if(!id.equals(shipmentUpdate.getId())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new CommonResponse<>(409, "Wrong id"));
        }
        Optional<Shipment> dbShipment = shipmentRepository.findById(shipmentUpdate.getId());

        if(dbShipment.isPresent()) {
            // check if administrator or if shipment is made by user
            if(!(principal.getClaimAsStringList("roles").contains("Administrator") || (dbShipment.get().getSender().getKcSubjectId() != null && dbShipment.get().getSender().getKcSubjectId().equals(principal.getClaimAsString("sub"))))){
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new CommonResponse<>(403, "Not allowed"));
            }
            // find status by status name, return bad request if status doesn't exist
            Optional<Status> status = statusRepository.findByStatus(shipmentUpdate.getStatus());
            if(!status.isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new CommonResponse<>(400, "Status doesn't exist"));

            }
            //create new shipment-status object
            ShipmentStatus shipmentStatus = new ShipmentStatus(status.get(),dbShipment.get());
            dbShipment.get().getShipmentStatuses().add(shipmentStatus);
            shipmentStatusRepository.save(shipmentStatus);
            returnShipment = shipmentRepository.save(dbShipment.get());
            return ResponseEntity
                    .ok()
                    .body(new CommonResponse<>(new ShipmentDTO(returnShipment)));
        }
        return ResponseEntity
                .badRequest()
                .body(new CommonResponse<>(400, "Bad input"));
    }

    //enpoint for getting customer shipments by id
    @GetMapping("/customer/{subjectId}")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Get customers shipments. Only registered users can access.")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<List<ShipmentDTO>>> getAccountShipments(@PathVariable String subjectId, @AuthenticationPrincipal Jwt principal) {
        //check if user is not registered or user is not admin, if not then return not allowed
        if(!(principal.getClaimAsString("sub").equals(subjectId) || principal.getClaimAsStringList("roles").contains("Administrator"))) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new CommonResponse<>(403, "Not allowed"));
        }

        Optional<Account> account = accountRepository.findByKcSubjectId(subjectId);
        List<Shipment> shipments = shipmentRepository.findAllBySender(account.get());
        List<ShipmentDTO> shipmentDTOs = shipments.stream().map(ShipmentDTO::new).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(shipmentDTOs));
    }

    //endpoint for getting completed shipments
    @GetMapping("/complete")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Get completed shipments. Only registered users can access.")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<List<ShipmentDTO>>> getCompletedShipments(@AuthenticationPrincipal Jwt principal) {

        //create list with shipments where status is completed
        List<Shipment> shipments = shipmentRepository.findAllByShipmentStatusEquals("COMPLETED");

        if(!principal.getClaimAsStringList("roles").contains("Administrator")){
            shipments = shipments.stream().filter(shipment -> (shipment.getSender().getKcSubjectId() != null && shipment.getSender().getKcSubjectId().equals(principal.getSubject()))).collect(Collectors.toList());
        }

        //return completed shipments as response object
        List<ShipmentDTO> shipmentDTOS = shipments.stream().map(ShipmentDTO::new).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(shipmentDTOS));
    }

    //endpoint for getting cancelled shipments
    @GetMapping("/cancelled")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Get cancelled shipments. Only registered users can access.")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<List<ShipmentDTO>>> getCancelledShipments(@AuthenticationPrincipal Jwt principal) {

        //create list with shipments where status is cancelled
        List<Shipment> shipments = shipmentRepository.findAllByShipmentStatusEquals("CANCELLED");

        if(!principal.getClaimAsStringList("roles").contains("Administrator")){
            shipments = shipments.stream().filter(shipment -> (shipment.getSender().getKcSubjectId() != null && shipment.getSender().getKcSubjectId().equals(principal.getSubject()))).collect(Collectors.toList());
        }
        //return shipment object where status is cancelled
        List<ShipmentDTO> shipmentDTOS = shipments.stream().map(ShipmentDTO::new).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(shipmentDTOS));
    }

    //endpoint for deleting shipment by id
    @DeleteMapping({"{id}"})
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Delete shipment by id. Only administrators can access.")
    @PreAuthorize("hasRole('Administrator')")
    public ResponseEntity<CommonResponse<String>> deleteShipment(@PathVariable Long id) {
        //delete shipment object by id
        shipmentRepository.deleteById(id);
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>("Shipment deleted"));
    }
    
    //see all current shipments and their status
    @GetMapping
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Get all shipments. Only registered users can access.")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<List<ShipmentDTO>>> getAllCurrentShipments(@AuthenticationPrincipal Jwt principal){
        List<String> roles = principal.getClaimAsStringList("roles");
        List<Shipment> shipments;
        List<ShipmentDTO> shipmentDTOS;
        //check if role is admin, if true, get all current shipments and their status.
        // Else find all shipments for the specific user.
        if(roles.contains("Administrator")) {
            shipments = shipmentRepository.findAllCurrentShipments();
        } else {
            Account sender = accountService.getAccountWithJwt(principal);
            shipments = shipmentRepository.findAllCurrentBySender(sender);
        }

        shipmentDTOS = shipments.stream().map(shipment -> new ShipmentDTO(shipment)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new CommonResponse<>(shipmentDTOS));
    }
}
