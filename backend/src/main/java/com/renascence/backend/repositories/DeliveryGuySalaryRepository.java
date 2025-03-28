package com.renascence.backend.repositories;

import com.renascence.backend.entities.DeliveryGuySalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryGuySalaryRepository extends JpaRepository<DeliveryGuySalary, Long> {

    List<DeliveryGuySalary> findByDeliveryGuyId(Long deliveryGuyId);
}
