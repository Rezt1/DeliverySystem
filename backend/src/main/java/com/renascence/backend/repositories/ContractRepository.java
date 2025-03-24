package com.renascence.backend.repositories;

import com.renascence.backend.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

}
