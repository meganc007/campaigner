package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.Continent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IContinentRepository extends JpaRepository<Continent, Integer> {
    Optional<Continent> findByName(String name);

    List<Continent> findByCampaign_Uuid(UUID uuid);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
