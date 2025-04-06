package com.renascence.backend.repositories;

import com.renascence.backend.entities.Delivery;
import com.renascence.backend.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findFirstByDeliveryGuyIdAndStatusOrderByCreationDateAsc(Long id, DeliveryStatus status);

}
