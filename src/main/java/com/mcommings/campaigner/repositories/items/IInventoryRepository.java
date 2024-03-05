package com.mcommings.campaigner.repositories.items;

import com.mcommings.campaigner.models.items.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IInventoryRepository extends JpaRepository<Inventory, Integer> {

    @Query("SELECT i FROM Inventory i WHERE " +
            "i.fk_person = :#{#inventory.fk_person} AND " +
            "i.fk_item = :#{#inventory.fk_item} AND " +
            "i.fk_weapon = :#{#inventory.fk_weapon} AND " +
            "i.fk_place = :#{#inventory.fk_place}")
    Optional<Inventory> inventoryExists(@Param("inventory") Inventory inventory);

    @Query("SELECT i FROM Inventory i WHERE i.fk_campaign_uuid = :uuid")
    List<Inventory> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT i FROM Inventory i WHERE i.fk_person = :id")
    List<Inventory> findByfk_person(@Param("id") Integer id);

    @Query("SELECT i FROM Inventory i WHERE i.fk_item = :id")
    List<Inventory> findByfk_item(@Param("id") Integer id);

    @Query("SELECT i FROM Inventory i WHERE i.fk_weapon = :id")
    List<Inventory> findByfk_weapon(@Param("id") Integer id);

    @Query("SELECT i FROM Inventory i WHERE i.fk_place = :id")
    List<Inventory> findByfk_place(@Param("id") Integer id);
}
