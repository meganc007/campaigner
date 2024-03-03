package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRegionRepository extends JpaRepository<Region, Integer> {

    Optional<Region> findByName(String name);

    @Query("SELECT r FROM Region r WHERE r.fk_campaign_uuid = :uuid")
    List<Region> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT r FROM Region r WHERE r.fk_country = :id")
    List<Region> findByfk_country(@Param("id") Integer id);

    @Query("SELECT r FROM Region r WHERE r.fk_climate = :id")
    List<Region> findByfk_climate(@Param("id") Integer id);
}
