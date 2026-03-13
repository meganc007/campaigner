package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByName(String name);

    List<Country> findByCampaign_Uuid(UUID uuid);

    List<Country> findByContinent_Id(Integer continentId);

    List<Country> findByGovernment_Id(Integer governmentId);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
