package se.experis.boxinator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.experis.boxinator.models.CommonResponse;
import se.experis.boxinator.models.dbo.Country;
import se.experis.boxinator.models.dbo.Status;
import se.experis.boxinator.repositories.StatusRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "${server.cors.application_origin}")
@SecurityRequirement(name = "keycloak")
@RequestMapping("/api/${app.version}/status")
public class StatusController {
    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    //endpoint for getting all shipment statuses
    @GetMapping
    @Operation(summary = "Get shipment statuses")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<List<Status>>> getAllStatuses(@AuthenticationPrincipal Jwt principal){
        //create list of statuses
        List<Status> statuses = statusRepository.findAll();
        // filter out statuses that arent completed or cancelled
        if(!principal.getClaimAsStringList("roles").contains("Administrator")) {
            statuses = statuses.stream().filter(status -> status.getStatus().equals("CANCELLED")).collect(Collectors.toList());
        }
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(statuses));
    }
}
