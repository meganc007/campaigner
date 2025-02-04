package com.mcommings.campaigner.repositories.items;

import com.mcommings.campaigner.entities.items.DiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDiceTypeRepository extends JpaRepository<DiceType, Integer> {

    Optional<DiceType> findByName(String name);
}
