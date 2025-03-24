package com.renascence.backend.repositories;

import com.renascence.backend.entities.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BonusRepository extends JpaRepository<Bonus, Long> {

}
