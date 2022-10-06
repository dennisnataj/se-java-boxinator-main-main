package se.experis.boxinator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.experis.boxinator.models.CommonResponse;
import se.experis.boxinator.models.dbo.Country;
import se.experis.boxinator.models.dto.CountryDTO;
import se.experis.boxinator.models.dto.ShipmentDTO;
import se.experis.boxinator.repositories.CountryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "${server.cors.application_origin}")
@RequestMapping("/api/${app.version}/settings/countries")
public class CountryController {

    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    //Retrieve country multiplier information
    @GetMapping
    @Operation(summary = "Get all country settings. Only registered users can access.")
    public ResponseEntity<CommonResponse<List<CountryDTO>>> getAllCountrySettings() {
        List<Country> countries = countryRepository.findAll();
        List<CountryDTO> countryDTO;

        countryDTO = countries.stream().map(country -> new CountryDTO(country)).collect(Collectors.toList());
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(countryDTO));
    }

    //endpoint for creating a new country
    @PostMapping
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Add/Create new country. Only administrators can access.")
    @PreAuthorize("hasRole('Administrator')")
    public ResponseEntity<CommonResponse<CountryDTO>> createCountry(@RequestBody CountryDTO countryDTO) {
        Country country = new Country();
        country.setName(countryDTO.getName());
        country.setShorthand(countryDTO.getShorthand());
        country.setMultiplier(country.getMultiplier());


        Country savedCountry = countryRepository.save(country);

        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(new CountryDTO(savedCountry)));
    }

    //Update a specific country multiplier
    @PutMapping("{id}")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Update country. Only administrators can access.")
    @PreAuthorize("hasRole('Administrator')")
    public ResponseEntity<CommonResponse<CountryDTO>> updateCountry(@PathVariable Long id, @RequestBody CountryDTO countryDTO) {


        if (!id.equals(countryDTO.getId())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new CommonResponse<>(409, "Wrong id"));
        }

        Optional<Country> savedCountry = countryRepository.findById(countryDTO.getId());
        if (!savedCountry.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new CommonResponse<>(400, "Wrong id"));
        }

        savedCountry.get().setMultiplier(countryDTO.getMultiplier());

        Country returnCountry = countryRepository.save(savedCountry.get());

        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(new CountryDTO(returnCountry)));
    }

}
