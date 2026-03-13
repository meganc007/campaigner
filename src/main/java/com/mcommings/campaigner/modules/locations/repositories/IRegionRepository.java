package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRegionRepository extends JpaRepository<Region, Integer> {

    Optional<Region> findByName(String name);

    List<Region> findByCampaign_Uuid(UUID uuid);

    List<Region> findByCountry_Id(Integer id);

    List<Region> findByClimate_Id(Integer id);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
