package com.mcommings.campaigner.items.repositories;

import com.mcommings.campaigner.items.entities.DamageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDamageTypeRepository extends JpaRepository<DamageType, Integer> {

    Optional<DamageType> findByName(String name);
}
