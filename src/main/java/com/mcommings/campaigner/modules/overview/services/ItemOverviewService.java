package com.mcommings.campaigner.modules.overview.services;

import com.mcommings.campaigner.modules.overview.dtos.ItemOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.ItemMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.ItemRepositoryFacade;
import com.mcommings.campaigner.modules.overview.services.interfaces.IItemOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemOverviewService implements IItemOverview {

    private final ItemMapperFacade itemMapperFacade;
    private final ItemRepositoryFacade itemRepositoryFacade;

    @Override
    public ItemOverviewDTO getItemOverview(UUID campaignId) {
        var damageTypes = itemRepositoryFacade.findDamageTypes()
                .stream().map(itemMapperFacade::toDamageTypeDto).toList();

        var diceTypes = itemRepositoryFacade.findDiceTypes()
                .stream().map(itemMapperFacade::toDiceTypeDto).toList();

        var inventories = itemRepositoryFacade.findInventoryByCampaign(campaignId)
                .stream().map(itemMapperFacade::toInventoryDto).toList();

        var items = itemRepositoryFacade.findItemsByCampaign(campaignId)
                .stream().map(itemMapperFacade::toItemDto).toList();

        var itemTypes = itemRepositoryFacade.findItemTypes()
                .stream().map(itemMapperFacade::toItemTypeDto).toList();

        var weapons = itemRepositoryFacade.findWeaponsByCampaign(campaignId)
                .stream().map(itemMapperFacade::toWeaponDto).toList();

        var weaponTypes = itemRepositoryFacade.findWeaponTypes()
                .stream().map(itemMapperFacade::toWeaponTypeDto).toList();

        return ItemOverviewDTO.builder()
                .damageTypes(damageTypes)
                .diceTypes(diceTypes)
                .inventories(inventories)
                .items(items)
                .itemTypes(itemTypes)
                .weapons(weapons)
                .weaponTypes(weaponTypes)
                .build();
    }
}
