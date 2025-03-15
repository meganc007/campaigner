package com.mcommings.campaigner.calendar.repositories;

import com.mcommings.campaigner.calendar.entities.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IMonthRepository extends JpaRepository<Month, Integer> {
    Optional<Month> findByName(String name);

    List<Month> findBySeason(String season);

    @Query("SELECT m FROM Month m WHERE m.fk_campaign_uuid = :uuid")
    List<Month> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
