package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.Continent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IContinentRepository extends JpaRepository<Continent, Integer> {
    Optional<Continent> findByName(String name);

    @Query("SELECT c FROM Continent c WHERE c.fk_campaign_uuid = :uuid")
    List<Continent> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

}
