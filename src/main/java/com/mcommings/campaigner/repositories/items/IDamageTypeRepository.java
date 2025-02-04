package com.mcommings.campaigner.repositories.items;

import com.mcommings.campaigner.entities.items.DamageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDamageTypeRepository extends JpaRepository<DamageType, Integer> {

    Optional<DamageType> findByName(String name);
}
