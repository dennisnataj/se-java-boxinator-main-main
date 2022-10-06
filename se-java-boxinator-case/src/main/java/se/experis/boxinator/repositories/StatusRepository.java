package se.experis.boxinator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.boxinator.models.dbo.Status;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByStatus(String status);
}
