package com.mcommings.campaigner.modules.items.repositories;

import com.mcommings.campaigner.modules.items.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByName(String name);

    List<Item> findByCampaign_Uuid(UUID uuid);

    List<Item> findByItemType_Id(Integer id);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
