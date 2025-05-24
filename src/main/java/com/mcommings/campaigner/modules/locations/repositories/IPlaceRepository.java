package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPlaceRepository extends JpaRepository<Place, Integer> {

    Optional<Place> findByName(String name);

    @Query("SELECT p FROM Place p WHERE p.fk_campaign_uuid = :uuid")
    List<Place> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT p FROM Place p WHERE p.fk_place_type = :id")
    List<Place> findByfk_place_type(@Param("id") Integer id);

    @Query("SELECT p FROM Place p WHERE p.fk_terrain = :id")
    List<Place> findByfk_terrain(@Param("id") Integer id);

    @Query("SELECT p FROM Place p WHERE p.fk_country = :id")
    List<Place> findByfk_country(@Param("id") Integer id);

    @Query("SELECT p FROM Place p WHERE p.fk_city = :id")
    List<Place> findByfk_city(@Param("id") Integer id);

    @Query("SELECT p FROM Place p WHERE p.fk_region = :id")
    List<Place> findByfk_region(@Param("id") Integer id);

    @Query("select distinct p.fk_place_type from Place p where p.fk_campaign_uuid = :uuid")
    List<Integer> findPlaceTypeIdsByCampaign(@Param("uuid") UUID uuid);

    @Query("select distinct p.fk_terrain from Place p where p.fk_campaign_uuid = :uuid")
    List<Integer> findTerrainIdsByCampaign(@Param("uuid") UUID uuid);
}
