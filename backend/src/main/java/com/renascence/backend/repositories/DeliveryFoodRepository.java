package com.renascence.backend.repositories;

import com.renascence.backend.entities.DeliveryFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryFoodRepository extends JpaRepository<DeliveryFood, Long> {

}
