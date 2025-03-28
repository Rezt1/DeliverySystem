package com.renascence.backend.repositories;

import com.renascence.backend.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByDeliveryGuyId(Long deliveryGuyId);
}
