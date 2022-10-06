package se.experis.boxinator.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import se.experis.boxinator.models.dbo.Account;
import se.experis.boxinator.repositories.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountWithJwt(Jwt principal) {
        // parse for email and subject
        String email = principal.getClaimAsString("email");
        String subjectId = principal.getClaimAsString("sub");

        // check if user exists by email
        Optional<Account> account = accountRepository.findByEmail(email);

        if(account.isPresent()) {
            // if guest account, set subjectId
            if(account.get().getKcSubjectId() == null) {
                account.get().setKcSubjectId(subjectId);
            }

            return account.get();
        }

        // if doesn't exist, create new user
        Account newAccount = new Account();
        newAccount.setEmail(email);
        newAccount.setKcSubjectId(subjectId);
        // and save it
        try {
            newAccount = accountRepository.save(newAccount);
        } catch (DataIntegrityViolationException e) {

            return null;
        }

        return newAccount;
    }

    public Account getGuestAccount(String email) {
        // check if user exist
        Optional<Account> account = accountRepository.findByEmail(email);

        if(account.isPresent()) {
            return account.get();
        }

        //create new user otherwise
        Account guestAccount = new Account(email);
        // and save it
        guestAccount = accountRepository.save(guestAccount);

        return guestAccount;
    }

}
