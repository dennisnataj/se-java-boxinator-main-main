package se.experis.boxinator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.experis.boxinator.models.dbo.ShipmentStatus;
import se.experis.boxinator.models.dbo.ShipmentStatusId;

public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, ShipmentStatusId> {
}
