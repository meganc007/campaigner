package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByName(String name);

    List<City> findByCampaign_Uuid(UUID uuid);

    List<City> findByWealth_Id(Integer id);

    List<City> findByCountry_Id(Integer id);

    List<City> findBySettlementType_Id(Integer id);

    List<City> findByGovernment_Id(Integer id);

    List<City> findByRegion_Id(Integer id);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
