package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.SettlementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISettlementTypeRepository extends JpaRepository<SettlementType, Integer> {
    Optional<SettlementType> findByName(String name);
}
