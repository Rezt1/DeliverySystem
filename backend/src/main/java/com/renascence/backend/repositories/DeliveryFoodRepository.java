package com.renascence.backend.repositories;

import com.renascence.backend.entities.DeliveryFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryFoodRepository extends JpaRepository<DeliveryFood, Long> {

    List<DeliveryFood> findByDeliveryId(Long deliveryId);
}
