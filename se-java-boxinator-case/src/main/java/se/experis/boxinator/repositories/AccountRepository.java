package se.experis.boxinator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.boxinator.models.dbo.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByKcSubjectId(String subjectId);
    Optional<Account> findByEmail(String email);
    List<Account> findByEmailContaining(String email);
}
