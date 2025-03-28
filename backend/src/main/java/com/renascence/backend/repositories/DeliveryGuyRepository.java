package com.renascence.backend.repositories;

import com.renascence.backend.entities.DeliveryGuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryGuyRepository extends JpaRepository<DeliveryGuy, Long> {

    @Query("SELECT d FROM DeliveryGuy d WHERE SIZE(d.deliveries) = (SELECT MIN(SIZE(d2.deliveries)) FROM DeliveryGuy d2)")
    List<DeliveryGuy> findLeastBusyDeliveryGuys();
}
