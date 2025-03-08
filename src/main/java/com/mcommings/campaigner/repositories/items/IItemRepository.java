package com.mcommings.campaigner.repositories.items;

import com.mcommings.campaigner.entities.items.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByName(String name);

    @Query("SELECT i FROM Item i WHERE i.fk_campaign_uuid = :uuid")
    List<Item> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT i FROM Item i WHERE i.fk_item_type = :id")
    List<Item> findByfk_item_type(@Param("id") Integer id);
}
