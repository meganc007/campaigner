package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ILandmarkRepository extends JpaRepository<Landmark, Integer> {
    Optional<Landmark> findByName(String name);

    @Query("SELECT l FROM Landmark l WHERE l.fk_campaign_uuid = :uuid")
    List<Landmark> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT l FROM Landmark l WHERE l.fk_region = :id")
    List<Landmark> findByfk_region(@Param("id") Integer id);

}
