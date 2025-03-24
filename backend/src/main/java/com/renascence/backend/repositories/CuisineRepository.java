package com.renascence.backend.repositories;

import com.renascence.backend.entities.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {

}
