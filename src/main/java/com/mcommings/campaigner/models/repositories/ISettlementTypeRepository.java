package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.SettlementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISettlementTypeRepository extends JpaRepository<SettlementType, Integer> {
}
