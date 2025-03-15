package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.Moon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IMoonRepository extends JpaRepository<Moon, Integer> {

    Optional<Moon> findByName(String name);

    @Query("SELECT m FROM Moon m WHERE m.fk_campaign_uuid = :uuid")
    List<Moon> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
