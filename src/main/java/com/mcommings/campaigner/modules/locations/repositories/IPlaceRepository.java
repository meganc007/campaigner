package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPlaceRepository extends JpaRepository<Place, Integer> {

    Optional<Place> findByName(String name);

    List<Place> findByCampaign_Uuid(UUID uuid);

    List<Place> findByPlaceType_Id(Integer id);

    List<Place> findByTerrain_Id(Integer id);

    List<Place> findByCountry_Id(Integer id);

    List<Place> findByCity_Id(Integer id);

    List<Place> findByRegion_Id(Integer id);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
