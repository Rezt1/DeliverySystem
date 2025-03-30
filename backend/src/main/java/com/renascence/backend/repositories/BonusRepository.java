package com.renascence.backend.repositories;

import com.renascence.backend.entities.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {

}
