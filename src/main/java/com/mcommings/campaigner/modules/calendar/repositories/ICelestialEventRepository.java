package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICelestialEventRepository extends JpaRepository<CelestialEvent, Integer> {

    Optional<CelestialEvent> findByName(String name);

    @Query("SELECT ce FROM CelestialEvent ce WHERE ce.fk_campaign_uuid = :uuid")
    List<CelestialEvent> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT ce FROM CelestialEvent ce WHERE ce.fk_moon = :id")
    List<CelestialEvent> findByfk_moon(@Param("id") Integer id);

    @Query("SELECT ce FROM CelestialEvent ce WHERE ce.fk_sun = :id")
    List<CelestialEvent> findByfk_sun(@Param("id") Integer id);

    @Query("SELECT ce FROM CelestialEvent ce WHERE ce.fk_month = :id")
    List<CelestialEvent> findByfk_month(@Param("id") Integer id);

    @Query("SELECT ce FROM CelestialEvent ce WHERE ce.fk_week = :id")
    List<CelestialEvent> findByfk_week(@Param("id") Integer id);

    @Query("SELECT ce FROM CelestialEvent ce WHERE ce.fk_day = :id")
    List<CelestialEvent> findByfk_day(@Param("id") Integer id);

    @Query("SELECT ce FROM CelestialEvent ce WHERE ce.event_year = :id")
    List<CelestialEvent> getByevent_year(int id);
}
