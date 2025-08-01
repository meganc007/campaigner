package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByName(String name);

    @Query("SELECT c FROM City c WHERE c.fk_campaign_uuid = :uuid")
    List<City> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT c FROM City c WHERE c.fk_wealth = :id")
    List<City> findByfk_wealth(@Param("id") Integer id);

    @Query("SELECT c FROM City c WHERE c.fk_country = :id")
    List<City> findByfk_country(@Param("id") Integer id);

    @Query("SELECT c FROM City c WHERE c.fk_settlement = :id")
    List<City> findByfk_settlement(@Param("id") Integer id);

    @Query("SELECT c FROM City c WHERE c.fk_government = :id")
    List<City> findByfk_government(@Param("id") Integer id);

    @Query("SELECT c FROM City c WHERE c.fk_region = :id")
    List<City> findByfk_region(@Param("id") Integer id);
}
