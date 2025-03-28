package com.renascence.backend.repositories;

import com.renascence.backend.entities.Delivery;
import com.renascence.backend.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByDeliveryGuyId(Long deliveryGuyId);
    List<Delivery> findByRestaurantId(Long restaurantId);
    List<Delivery> findByDeliveryGuyIdAndStatus(long id, DeliveryStatus deliveryStatus);
}
