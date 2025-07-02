package com.mcommings.campaigner.modules.items.repositories;

import com.mcommings.campaigner.modules.items.entities.DamageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDamageTypeRepository extends JpaRepository<DamageType, Integer> {

    Optional<DamageType> findByName(String name);

    List<DamageType> findAllByOrderByNameAsc();
}
