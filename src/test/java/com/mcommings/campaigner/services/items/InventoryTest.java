package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.items.dtos.inventories.CreateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.UpdateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.ViewInventoryDTO;
import com.mcommings.campaigner.modules.items.entities.Inventory;
import com.mcommings.campaigner.modules.items.mappers.InventoryMapper;
import com.mcommings.campaigner.modules.items.repositories.IInventoryRepository;
import com.mcommings.campaigner.modules.items.services.InventoryService;
import com.mcommings.campaigner.setup.items.factories.ItemsTestDataFactory;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryTest {

    @Mock
    private IInventoryRepository inventoryRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory inventory;
    private ViewInventoryDTO viewDto;
    private CreateInventoryDTO createDto;
    private UpdateInventoryDTO updateDto;

    @BeforeEach
    void setUp() {
        inventory = ItemsTestDataFactory.inventory();
        viewDto = ItemsTestDataFactory.viewInventoryDTO();
        createDto = ItemsTestDataFactory.createInventoryDTO();
        updateDto = ItemsTestDataFactory.updateInventoryDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));
        when(inventoryMapper.toDto(inventory)).thenReturn(viewDto);

        List<ViewInventoryDTO> result = inventoryService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(inventoryRepository).findAll();
        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void getInventoriesByCampaignUUID_returnsMappedList() {

        when(inventoryRepository.findByCampaign_Uuid(ItemsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(inventory));

        when(inventoryMapper.toDto(inventory))
                .thenReturn(viewDto);

        List<ViewInventoryDTO> result =
                inventoryService.getInventoriesByCampaignUUID(
                        ItemsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(inventoryRepository)
                .findByCampaign_Uuid(ItemsTestConstants.CAMPAIGN_UUID);

        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void getInventoriesByPersonId_returnsMappedList() {
        when(inventoryRepository.findByPerson_Id(ItemsTestConstants.ITEM_ID))
                .thenReturn(List.of(inventory));

        when(inventoryMapper.toDto(inventory))
                .thenReturn(viewDto);

        List<ViewInventoryDTO> result =
                inventoryService.getInventoriesByPersonId(
                        ItemsTestConstants.PERSON_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(inventoryRepository)
                .findByPerson_Id((ItemsTestConstants.PERSON_ID));

        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void getInventoriesByItemId_returnsMappedList() {
        when(inventoryRepository.findByItem_Id(ItemsTestConstants.ITEM_ID))
                .thenReturn(List.of(inventory));

        when(inventoryMapper.toDto(inventory))
                .thenReturn(viewDto);

        List<ViewInventoryDTO> result =
                inventoryService.getInventoriesByItemId(
                        ItemsTestConstants.ITEM_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(inventoryRepository)
                .findByItem_Id((ItemsTestConstants.ITEM_ID));

        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void getInventoriesByWeaponId_returnsMappedList() {
        when(inventoryRepository.findByWeapon_Id(ItemsTestConstants.WEAPON_ID))
                .thenReturn(List.of(inventory));

        when(inventoryMapper.toDto(inventory))
                .thenReturn(viewDto);

        List<ViewInventoryDTO> result =
                inventoryService.getInventoriesByWeaponId(
                        ItemsTestConstants.WEAPON_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(inventoryRepository)
                .findByWeapon_Id((ItemsTestConstants.WEAPON_ID));

        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void getInventoriesByPlaceId_returnsMappedList() {
        when(inventoryRepository.findByPlace_Id(LocationsTestConstants.PLACE_ID))
                .thenReturn(List.of(inventory));

        when(inventoryMapper.toDto(inventory))
                .thenReturn(viewDto);

        List<ViewInventoryDTO> result =
                inventoryService.getInventoriesByPlaceId(
                        LocationsTestConstants.PLACE_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(inventoryRepository)
                .findByPlace_Id((LocationsTestConstants.PLACE_ID));

        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.of(inventory));

        when(inventoryMapper.toDto(inventory))
                .thenReturn(viewDto);

        ViewInventoryDTO result = inventoryService.getById(inventory.getId());

        assertEquals(viewDto, result);

        verify(inventoryRepository).findById(inventory.getId());
        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.getById(inventory.getId())
        );

        verify(inventoryRepository).findById(inventory.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(inventoryMapper.toEntity(createDto)).thenReturn(inventory);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryMapper.toDto(inventory)).thenReturn(viewDto);

        ViewInventoryDTO result = inventoryService.create(createDto);

        assertEquals(viewDto, result);

        verify(inventoryMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(inventoryRepository).save(inventory);
        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(inventoryRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(inventory));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(inventoryRepository.save(inventory)).thenReturn(inventory);
        when(inventoryMapper.toDto(inventory)).thenReturn(viewDto);

        ViewInventoryDTO result = inventoryService.update(updateDto);

        assertEquals(viewDto, result);

        verify(inventoryRepository).findById(updateDto.getId());
        verify(inventoryMapper).updateInventoryFromDto(updateDto, inventory);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(inventoryRepository).save(inventory);
        verify(inventoryMapper).toDto(inventory);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(inventoryRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.update(updateDto)
        );

        verify(inventoryRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        inventoryService.delete(inventory.getId());

        verify(inventoryRepository).deleteById(inventory.getId());
    }
}
