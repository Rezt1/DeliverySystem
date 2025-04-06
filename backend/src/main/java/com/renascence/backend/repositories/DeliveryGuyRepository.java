package com.renascence.backend.repositories;

import com.renascence.backend.entities.DeliveryGuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryGuyRepository extends JpaRepository<DeliveryGuy, Long> {

}
