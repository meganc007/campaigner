package com.mcommings.campaigner.modules.items.repositories;

import com.mcommings.campaigner.modules.items.entities.WeaponType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWeaponTypeRepository extends JpaRepository<WeaponType, Integer> {

    Optional<WeaponType> findByName(String name);

    List<WeaponType> findAllByOrderByNameAsc();
}
