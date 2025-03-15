package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IWeekRepository extends JpaRepository<Week, Integer> {
    @Query("SELECT w FROM Week w WHERE w.fk_month = :id")
    List<Week> findByfk_month(@Param("id") Integer id);

    @Query("SELECT w FROM Week w WHERE w.fk_campaign_uuid = :uuid")
    List<Week> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
