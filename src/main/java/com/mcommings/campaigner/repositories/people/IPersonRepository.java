package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.entities.people.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByFirstName(String firstName);

    Optional<Person> findByLastName(String lastName);

    @Query("SELECT p FROM Person p WHERE " +
            "p.firstName = :#{#person.firstName} AND " +
            "p.lastName = :#{#person.lastName} AND " +
            "p.fk_race = :#{#person.fk_race}")
    Optional<Person> personExists(@Param("person") Person person);

    @Query("SELECT p FROM Person p WHERE p.fk_race = :id")
    List<Person> findByfk_race(Integer id);

    @Query("SELECT p FROM Person p WHERE p.fk_wealth = :id")
    List<Person> findByfk_wealth(Integer id);

    @Query("SELECT p FROM Person p WHERE p.fk_ability_score = :id")
    List<Person> findByfk_ability_score(Integer id);

    @Query("SELECT p FROM Person p WHERE p.fk_campaign_uuid = :uuid")
    List<Person> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

}
