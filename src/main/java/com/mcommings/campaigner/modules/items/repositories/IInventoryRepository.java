package com.mcommings.campaigner.modules.items.repositories;

import com.mcommings.campaigner.modules.items.entities.Inventory;
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

    List<Inventory> findByCampaign_Uuid(UUID uuid);

    List<Inventory> findByPerson_Id(Integer id);

    List<Inventory> findByItem_Id(Integer id);

    List<Inventory> findByWeapon_Id(Integer id);

    List<Inventory> findByPlace_Id(Integer id);
}
