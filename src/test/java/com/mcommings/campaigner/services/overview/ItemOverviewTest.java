package com.mcommings.campaigner.services.overview;

import com.mcommings.campaigner.modules.items.dtos.DamageTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.modules.overview.dtos.ItemOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.ItemMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.ItemRepositoryFacade;
import com.mcommings.campaigner.modules.overview.services.ItemOverviewService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemOverviewTest {

    @Mock
    private ItemRepositoryFacade itemRepositoryFacade;

    @Mock
    private ItemMapperFacade itemMapperFacade;

    @InjectMocks
    private ItemOverviewService itemOverviewService;

    @Test
    void whenCampaignUUIDIsValid_getItemOverview_returnsAggregatedItemData() {
        UUID campaignId = UUID.randomUUID();

        // Mock data
        DamageType damageType = new DamageType(1, "bludgeon", "bonk");
        DamageTypeDTO damageTypeDTO = new DamageTypeDTO(1, "bludgeon", "bonk");

        when(itemRepositoryFacade.findDamageTypes()).thenReturn(List.of(damageType));
        when(itemMapperFacade.toDamageTypeDto(damageType)).thenReturn(damageTypeDTO);

        ItemOverviewDTO result = itemOverviewService.getItemOverview(campaignId);

        assertNotNull(result);
        assertEquals(1, result.getDamageTypes().size());
        assertEquals("bludgeon", result.getDamageTypes().get(0).getName());

        verify(itemRepositoryFacade).findDamageTypes();
        verify(itemMapperFacade).toDamageTypeDto(damageType);
    }

    @Test
    void whenCampaignUUIDIsNotValid_getItemOverview_returnsEmptyList() {
        UUID invalidCampaignId = UUID.randomUUID();

        when(itemRepositoryFacade.findDamageTypes()).thenReturn(Collections.emptyList());
        when(itemRepositoryFacade.findDiceTypes()).thenReturn(Collections.emptyList());
        when(itemRepositoryFacade.findInventoryByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(itemRepositoryFacade.findItemsByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(itemRepositoryFacade.findItemTypes()).thenReturn(Collections.emptyList());
        when(itemRepositoryFacade.findWeaponsByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(itemRepositoryFacade.findWeaponTypes()).thenReturn(Collections.emptyList());

        ItemOverviewDTO result = itemOverviewService.getItemOverview(invalidCampaignId);

        assertNotNull(result);
        assertTrue(result.getDamageTypes().isEmpty());
        assertTrue(result.getDiceTypes().isEmpty());
        assertTrue(result.getInventories().isEmpty());
        assertTrue(result.getItems().isEmpty());
        assertTrue(result.getItemTypes().isEmpty());
        assertTrue(result.getWeapons().isEmpty());
        assertTrue(result.getWeaponTypes().isEmpty());

        verify(itemRepositoryFacade).findDamageTypes();
        verify(itemRepositoryFacade).findDiceTypes();
        verify(itemRepositoryFacade).findInventoryByCampaign(invalidCampaignId);
        verify(itemRepositoryFacade).findItemsByCampaign(invalidCampaignId);
        verify(itemRepositoryFacade).findItemTypes();
        verify(itemRepositoryFacade).findWeaponsByCampaign(invalidCampaignId);
        verify(itemRepositoryFacade).findWeaponTypes();
    }
}
