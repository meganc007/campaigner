package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.models.people.GenericMonster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IGenericMonsterRepository extends JpaRepository<GenericMonster, Integer> {

    Optional<GenericMonster> findByName(String name);

    @Query("SELECT g FROM GenericMonster g WHERE g.fk_ability_score = :id")
    List<GenericMonster> findByfk_ability_score(Integer id);
}
