package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IDayRepository extends JpaRepository<Day, Integer> {

    Optional<Day> findByName(String name);

    @Query("SELECT d FROM Day d WHERE d.fk_week = :id")
    List<Day> findByfk_week(@Param("id") Integer id);

    @Query("SELECT d FROM Day d WHERE d.fk_campaign_uuid = :uuid")
    List<Day> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
