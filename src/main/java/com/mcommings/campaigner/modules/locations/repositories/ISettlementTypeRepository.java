package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISettlementTypeRepository extends JpaRepository<SettlementType, Integer> {
    Optional<SettlementType> findByName(String name);
}
