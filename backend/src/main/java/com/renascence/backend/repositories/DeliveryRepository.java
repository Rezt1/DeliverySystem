package com.renascence.backend.repositories;

import com.renascence.backend.entities.Delivery;
import com.renascence.backend.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {


}
