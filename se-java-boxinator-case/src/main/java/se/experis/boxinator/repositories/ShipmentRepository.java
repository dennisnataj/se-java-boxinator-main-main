package se.experis.boxinator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.experis.boxinator.models.dbo.Account;
import se.experis.boxinator.models.dbo.Shipment;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    //find all shipments where the latest status is provided
    @Query("""
        select s, sta from ShipmentStatus ss
            join Shipment s on ss.shipment.id = s.id
            join Status sta on ss.status.id = sta.id
            where ss.changedTime in (select max (s2.changedTime)
                                    from ShipmentStatus s2
                                    group by s2.shipment)
            and ss.status.status = ?1
            group by s, sta
            order by s.id
        """)
    List<Shipment> findAllByShipmentStatusEquals(String status);
    //find all shipments where the latest status is not completed or cancelled
    @Query("""
        select s from Shipment s
            join ShipmentStatus ss on ss.shipment.id = s.id
            join Status s2 on ss.status.id = s2.id
            where ss.changedTime in (select max(s3.changedTime)
                                    from ShipmentStatus s3
                                    group by s3.shipment)
            and (s2.id <> 4 and s2.id <> 5)
            order by s.id
        """)
    List<Shipment> findAllCurrentShipments();
    List<Shipment> findAllBySender(Account sender);

    //find all shipments where the latest status is not completed or cancelled by sender
    @Query("""
        select s from Shipment s
            join ShipmentStatus ss on ss.shipment.id = s.id
            join Status sta on ss.status.id = sta.id
            where ss.changedTime in (select max(s2.changedTime)
                                     from ShipmentStatus s2
                                     group by s2.shipment)
            and (sta.id <> 4 and sta.id <> 5)
            and s.sender = ?1
            order by s.id
        """)
    List<Shipment> findAllCurrentBySender(Account sender);
}
