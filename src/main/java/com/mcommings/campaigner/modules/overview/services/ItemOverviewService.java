package com.mcommings.campaigner.modules.overview.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.items.dtos.InventoryDTO;
import com.mcommings.campaigner.modules.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.modules.overview.dtos.ItemOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.ItemMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.ItemRepositoryFacade;
import com.mcommings.campaigner.modules.overview.helpers.InventoryOverview;
import com.mcommings.campaigner.modules.overview.services.interfaces.IItemOverview;
import com.mcommings.campaigner.modules.people.dtos.PersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        var inventoryOverviews = mapInventoryDTOtoInventoryOverview(inventories);

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
                .inventories(inventoryOverviews)
                .items(items)
                .itemTypes(itemTypes)
                .weapons(weapons)
                .weaponTypes(weaponTypes)
                .build();
    }

    private List<InventoryOverview> mapInventoryDTOtoInventoryOverview(List<InventoryDTO> inventories) {
        List<InventoryOverview> overviews = new ArrayList<>();
        for (InventoryDTO inventory : inventories) {
            Optional<PersonDTO> person = getPerson(inventory);
            Optional<PlaceDTO> place = getPlace(inventory);

            String personName = setPersonName(person);
            String placeName = setPlaceName(place);

            InventoryOverview io = InventoryOverview.builder()
                    .id(inventory.getId())
                    .fk_campaign_uuid(inventory.getFk_campaign_uuid())
                    .fk_person(inventory.getFk_person())
                    .fk_weapon(inventory.getFk_weapon())
                    .fk_place(inventory.getFk_place())
                    .personName(personName)
                    .placeName(placeName)
                    .build();

            overviews.add(io);
        }
        return overviews;
    }

    private Optional<PlaceDTO> getPlace(InventoryDTO inventory) {
        if (inventory.getFk_place() != null) {
            return itemRepositoryFacade.findPlace(inventory.getFk_place()).map(itemMapperFacade::toPlaceDto);
        }
        return Optional.empty();
    }

    private Optional<PersonDTO> getPerson(InventoryDTO inventory) {
        if (inventory.getFk_person() != null) {
            return itemRepositoryFacade.findPerson(inventory.getFk_person()).map(itemMapperFacade::toPersonDto);
        }
        return Optional.empty();
    }

    private String setPersonName(Optional<PersonDTO> person) {
        if (!person.isEmpty()) {
            return RepositoryHelper.nameIsNullOrEmpty(person.get().getLastName()) ?
                    person.get().getFirstName()
                    : person.get().getFirstName() + " " + person.get().getLastName();
        }
        return null;
    }

    private String setPlaceName(Optional<PlaceDTO> place) {
        if(!place.isEmpty()) {
            return RepositoryHelper.nameIsNullOrEmpty(place.get().getName()) ? "" : place.get().getName();
        }
        return null;
    }
}
