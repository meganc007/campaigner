package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.models.people.EventPlacePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IEventPlacePersonRepository extends JpaRepository<EventPlacePerson, Integer> {

    @Query("SELECT e FROM EventPlacePerson e WHERE " +
            "e.fk_event = :#{#eventPlacePerson.fk_event} AND " +
            "e.fk_place = :#{#eventPlacePerson.fk_place} AND " +
            "e.fk_person = :#{#eventPlacePerson.fk_person}")
    Optional<EventPlacePerson> eventPlacePersonExists(@Param("eventPlacePerson") EventPlacePerson eventPlacePerson);

    @Query("SELECT e FROM EventPlacePerson e WHERE e.fk_campaign_uuid = :uuid")
    List<EventPlacePerson> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT e FROM EventPlacePerson e WHERE e.fk_event = :id")
    List<EventPlacePerson> findByfk_event(@Param("id") Integer id);

    @Query("SELECT e FROM EventPlacePerson e WHERE e.fk_place = :id")
    List<EventPlacePerson> findByfk_place(@Param("id") Integer id);

    @Query("SELECT e FROM EventPlacePerson e WHERE e.fk_person = :id")
    List<EventPlacePerson> findByfk_person(@Param("id") Integer id);
}
