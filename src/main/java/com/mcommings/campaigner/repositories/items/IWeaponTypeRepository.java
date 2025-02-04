package com.mcommings.campaigner.repositories.items;

import com.mcommings.campaigner.entities.items.WeaponType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IWeaponTypeRepository extends JpaRepository<WeaponType, Integer> {

    Optional<WeaponType> findByName(String name);
}
