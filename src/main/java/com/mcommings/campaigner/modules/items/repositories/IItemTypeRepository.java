package com.mcommings.campaigner.modules.items.repositories;

import com.mcommings.campaigner.modules.items.entities.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IItemTypeRepository extends JpaRepository<ItemType, Integer> {

    Optional<ItemType> findByName(String name);

    List<ItemType> findAllByOrderByNameAsc();
}
