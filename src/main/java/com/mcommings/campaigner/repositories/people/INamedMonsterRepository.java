package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.models.people.NamedMonster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INamedMonsterRepository extends JpaRepository<NamedMonster, Integer> {

    Optional<NamedMonster> findByFirstName(String firstName);

    Optional<NamedMonster> findByLastName(String lastName);

    @Query("SELECT n FROM NamedMonster n WHERE " +
            "n.firstName = :#{#monster.firstName} AND " +
            "n.lastName = :#{#monster.lastName} AND " +
            "n.fk_generic_monster = :#{#monster.fk_generic_monster}")
    Optional<NamedMonster> monsterExists(@Param("monster") NamedMonster monster);

    @Query("SELECT n FROM NamedMonster n WHERE n.fk_wealth = :id")
    List<NamedMonster> findByfk_wealth(Integer id);

    @Query("SELECT n FROM NamedMonster n WHERE n.fk_ability_score = :id")
    List<NamedMonster> findByfk_ability_score(Integer id);

    @Query("SELECT n FROM NamedMonster n WHERE n.fk_generic_monster = :id")
    List<NamedMonster> findByfk_generic_monster(Integer id);
}
