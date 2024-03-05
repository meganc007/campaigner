package com.mcommings.campaigner.repositories.items;

import com.mcommings.campaigner.models.items.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IWeaponRepository extends JpaRepository<Weapon, Integer> {

    Optional<Weapon> findByName(String name);

    @Query("SELECT w FROM Weapon w WHERE w.fk_campaign_uuid = :uuid")
    List<Weapon> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT w FROM Weapon w WHERE w.fk_weapon_type = :id")
    List<Weapon> findByfk_weapon_type(@Param("id") Integer id);

    @Query("SELECT w FROM Weapon w WHERE w.fk_damage_type = :id")
    List<Weapon> findByfk_damage_type(@Param("id") Integer id);

    @Query("SELECT w FROM Weapon w WHERE w.fk_dice_type = :id")
    List<Weapon> findByfk_dice_type(@Param("id") Integer id);
}
