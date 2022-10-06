package se.experis.boxinator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import se.experis.boxinator.models.CommonResponse;
import se.experis.boxinator.models.dbo.Account;
import se.experis.boxinator.models.dbo.Country;
import se.experis.boxinator.models.dto.AccountUpdate;
import se.experis.boxinator.repositories.AccountRepository;
import se.experis.boxinator.repositories.CountryRepository;

import java.util.Optional;


@RestController
@CrossOrigin(origins = "${server.cors.application_origin}")
@SecurityRequirement(name = "keycloak")
@RequestMapping("/api/${app.version}/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final CountryRepository countryRepository;

    public AccountController(AccountRepository accountRepository, CountryRepository countryRepository) {
        this.accountRepository = accountRepository;
        this.countryRepository = countryRepository;
    }

    //endpoint for getting a customer account by id
    @GetMapping("{subjectId}")
    @Operation(summary = "Get account by id. Only registered users can access.")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<Account>> getAccountBySubjectId(@PathVariable String subjectId, @AuthenticationPrincipal Jwt principal) {
        if (!(principal.getClaimAsStringList("roles").contains("Administrator") || principal.getClaimAsString("sub").equals(subjectId))){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new CommonResponse<>(403,"Not allowed"));
        }

        Optional<Account> account = accountRepository.findByKcSubjectId(subjectId);
        if (account.isPresent()) {
            return ResponseEntity
                    .ok()
                    .body(new CommonResponse<>(account.get()));
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CommonResponse<>(404, "account doesn't exist"));
    }

    //endpoint for updating account information by specific account id
    @PutMapping("{subjectId}")
    @Operation(summary = "Update account by id. Only registered users can access.")
    @PreAuthorize("hasRole('Customer')")
    public ResponseEntity<CommonResponse<AccountUpdate>> updateAccount(@PathVariable String subjectId,@AuthenticationPrincipal Jwt principal, @RequestBody AccountUpdate accountUpdate) {
        Optional<Account> savedAccount;

        //check if user is not authenticated, if not, return message "not allowed"
        if(!principal.getClaimAsString("sub").equals(subjectId)){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new CommonResponse<>(403, "Not allowed"));
        }
        savedAccount = accountRepository.findByKcSubjectId(subjectId);

        savedAccount.get().setDob(accountUpdate.getDob());
        savedAccount.get().setPostalCode(accountUpdate.getPostalCode());
        savedAccount.get().setContactNumber(accountUpdate.getContactNumber());
        Optional<Country> country = countryRepository.findByName(accountUpdate.getCountry());

        if(country.isPresent()) {
            savedAccount.get().setCountry(country.get());
        }

        Account returnAccount = accountRepository.save(savedAccount.get());
        return ResponseEntity
                .ok()
                .body(new CommonResponse<>(new AccountUpdate(returnAccount)));
    }

}