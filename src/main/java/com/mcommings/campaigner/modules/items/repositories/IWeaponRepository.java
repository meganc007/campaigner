package com.mcommings.campaigner.modules.items.repositories;

import com.mcommings.campaigner.modules.items.entities.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IWeaponRepository extends JpaRepository<Weapon, Integer> {

    Optional<Weapon> findByName(String name);

    List<Weapon> findByCampaign_Uuid(UUID uuid);

    List<Weapon> findByWeaponType_Id(Integer id);

    List<Weapon> findByDamageType_Id(Integer id);

    List<Weapon> findByDiceType_Id(Integer id);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
