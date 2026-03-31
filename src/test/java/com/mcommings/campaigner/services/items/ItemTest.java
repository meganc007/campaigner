package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.items.dtos.items.CreateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.UpdateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.ViewItemDTO;
import com.mcommings.campaigner.modules.items.entities.Item;
import com.mcommings.campaigner.modules.items.mappers.ItemMapper;
import com.mcommings.campaigner.modules.items.repositories.IItemRepository;
import com.mcommings.campaigner.modules.items.services.ItemService;
import com.mcommings.campaigner.setup.items.factories.ItemsTestDataFactory;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;
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
public class ItemTest {

    @Mock
    private IItemRepository itemRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    private Item item;
    private ViewItemDTO viewDto;
    private CreateItemDTO createDto;
    private UpdateItemDTO updateDto;

    @BeforeEach
    void setUp() {
        item = ItemsTestDataFactory.item();
        viewDto = ItemsTestDataFactory.viewItemDTO();
        createDto = ItemsTestDataFactory.createItemDTO();
        updateDto = ItemsTestDataFactory.updateItemDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemMapper.toDto(item)).thenReturn(viewDto);

        List<ViewItemDTO> result = itemService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(itemRepository).findAll();
        verify(itemMapper).toDto(item);
    }

    @Test
    void getItemsByCampaignUUID_returnsMappedList() {

        when(itemRepository.findByCampaign_Uuid(ItemsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(item));

        when(itemMapper.toDto(item))
                .thenReturn(viewDto);

        List<ViewItemDTO> result =
                itemService.getItemsByCampaignUUID(
                        ItemsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(itemRepository)
                .findByCampaign_Uuid(ItemsTestConstants.CAMPAIGN_UUID);

        verify(itemMapper).toDto(item);
    }

    @Test
    void getItemsByItemTypeId_returnsMappedList() {
        when(itemRepository.findByItemType_Id(ItemsTestConstants.ITEM_TYPE_ID))
                .thenReturn(List.of(item));

        when(itemMapper.toDto(item))
                .thenReturn(viewDto);

        List<ViewItemDTO> result =
                itemService.getItemsByItemTypeId(
                        ItemsTestConstants.ITEM_TYPE_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(itemRepository)
                .findByItemType_Id((ItemsTestConstants.ITEM_TYPE_ID));

        verify(itemMapper).toDto(item);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));

        when(itemMapper.toDto(item))
                .thenReturn(viewDto);

        ViewItemDTO result = itemService.getById(item.getId());

        assertEquals(viewDto, result);

        verify(itemRepository).findById(item.getId());
        verify(itemMapper).toDto(item);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> itemService.getById(item.getId())
        );

        verify(itemRepository).findById(item.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(itemMapper.toEntity(createDto)).thenReturn(item);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(viewDto);

        ViewItemDTO result = itemService.create(createDto);

        assertEquals(viewDto, result);

        verify(itemMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(itemRepository).save(item);
        verify(itemMapper).toDto(item);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(itemRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(item));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(viewDto);

        ViewItemDTO result = itemService.update(updateDto);

        assertEquals(viewDto, result);

        verify(itemRepository).findById(updateDto.getId());
        verify(itemMapper).updateItemFromDto(updateDto, item);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(itemRepository).save(item);
        verify(itemMapper).toDto(item);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(itemRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> itemService.update(updateDto)
        );

        verify(itemRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        itemService.delete(item.getId());

        verify(itemRepository).deleteById(item.getId());
    }
}
