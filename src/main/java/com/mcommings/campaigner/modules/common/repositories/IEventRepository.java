package com.mcommings.campaigner.modules.common.repositories;

import com.mcommings.campaigner.modules.common.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {

    Optional<Event> findByName(String name);

    @Query("SELECT e FROM Event e WHERE e.fk_campaign_uuid = :uuid")
    List<Event> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT e FROM Event e WHERE e.fk_month = :id")
    List<Event> findByfk_month(@Param("id") Integer id);

    @Query("SELECT e FROM Event e WHERE e.fk_week = :id")
    List<Event> findByfk_week(@Param("id") Integer id);

    @Query("SELECT e FROM Event e WHERE e.fk_day = :id")
    List<Event> findByfk_day(@Param("id") Integer id);

    @Query("SELECT e FROM Event e WHERE e.fk_city = :id")
    List<Event> findByfk_city(@Param("id") Integer id);

    @Query("SELECT e FROM Event e WHERE e.fk_continent = :id")
    List<Event> findByfk_continent(@Param("id") Integer id);

    @Query("SELECT e FROM Event e WHERE e.fk_country = :id")
    List<Event> findByfk_country(@Param("id") Integer id);
    
}
