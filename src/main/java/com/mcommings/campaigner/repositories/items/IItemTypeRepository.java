package com.mcommings.campaigner.repositories.items;

import com.mcommings.campaigner.entities.items.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IItemTypeRepository extends JpaRepository<ItemType, Integer> {

    Optional<ItemType> findByName(String name);
}
