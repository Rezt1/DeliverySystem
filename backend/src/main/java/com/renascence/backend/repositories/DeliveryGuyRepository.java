package com.renascence.backend.repositories;

import com.renascence.backend.entities.DeliveryGuy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryGuyRepository extends JpaRepository<DeliveryGuy, Long> {

}
