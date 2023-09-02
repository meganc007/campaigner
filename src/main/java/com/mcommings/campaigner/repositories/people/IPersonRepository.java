package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.models.people.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    List<Person> findByfk_race(int id);

    @Query("SELECT p FROM Person p WHERE p.fk_wealth = :id")
    List<Person> findByfk_wealth(int id);

    @Query("SELECT p FROM Person p WHERE p.fk_ability_score = :id")
    List<Person> findByfk_ability_score(int id);

}
