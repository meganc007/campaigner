package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.entities.locations.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByName(String name);

    @Query("SELECT c FROM Country c WHERE c.fk_campaign_uuid = :uuid")
    List<Country> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT c FROM Country c WHERE c.fk_continent = :id")
    List<Country> findByfk_continent(@Param("id") Integer id);

    @Query("SELECT c FROM Country c WHERE c.fk_government = :id")
    List<Country> findByfk_government(@Param("id") Integer id);
}
