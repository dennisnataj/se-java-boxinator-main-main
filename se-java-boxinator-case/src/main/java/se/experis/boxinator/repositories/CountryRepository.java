package se.experis.boxinator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.boxinator.models.dbo.Country;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}
