package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.item_types.CreateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.UpdateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.ViewItemTypeDTO;
import com.mcommings.campaigner.modules.items.entities.ItemType;
import com.mcommings.campaigner.modules.items.mappers.ItemTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IItemTypeRepository;
import com.mcommings.campaigner.modules.items.services.ItemTypeService;
import com.mcommings.campaigner.setup.items.factories.ItemsTestDataFactory;
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
public class ItemTypeTest {

    @Mock
    private IItemTypeRepository itemTypeRepository;

    @Mock
    private ItemTypeMapper itemTypeMapper;

    @InjectMocks
    private ItemTypeService itemTypeService;

    private ItemType itemType;
    private ViewItemTypeDTO viewDto;
    private CreateItemTypeDTO createDto;
    private UpdateItemTypeDTO updateDto;

    @BeforeEach
    void setUp() {
        itemType = ItemsTestDataFactory.itemType();
        viewDto = ItemsTestDataFactory.viewItemTypeDTO();
        createDto = ItemsTestDataFactory.createItemTypeDTO();
        updateDto = ItemsTestDataFactory.updateItemTypeDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(itemTypeRepository.findAll()).thenReturn(List.of(itemType));
        when(itemTypeMapper.toDto(itemType)).thenReturn(viewDto);

        List<ViewItemTypeDTO> result = itemTypeService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(itemTypeRepository).findAll();
        verify(itemTypeMapper).toDto(itemType);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(itemTypeRepository.findById(itemType.getId()))
                .thenReturn(Optional.of(itemType));

        when(itemTypeMapper.toDto(itemType))
                .thenReturn(viewDto);

        ViewItemTypeDTO result = itemTypeService.getById(itemType.getId());

        assertEquals(viewDto, result);

        verify(itemTypeRepository).findById(itemType.getId());
        verify(itemTypeMapper).toDto(itemType);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(itemTypeRepository.findById(itemType.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> itemTypeService.getById(itemType.getId())
        );

        verify(itemTypeRepository).findById(itemType.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(itemTypeMapper.toEntity(createDto)).thenReturn(itemType);

        when(itemTypeRepository.save(itemType)).thenReturn(itemType);
        when(itemTypeMapper.toDto(itemType)).thenReturn(viewDto);

        ViewItemTypeDTO result = itemTypeService.create(createDto);

        assertEquals(viewDto, result);

        verify(itemTypeMapper).toEntity(createDto);
        verify(itemTypeRepository).save(itemType);
        verify(itemTypeMapper).toDto(itemType);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(itemTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(itemType));

        when(itemTypeRepository.save(itemType)).thenReturn(itemType);
        when(itemTypeMapper.toDto(itemType)).thenReturn(viewDto);

        ViewItemTypeDTO result = itemTypeService.update(updateDto);

        assertEquals(viewDto, result);

        verify(itemTypeRepository).findById(updateDto.getId());
        verify(itemTypeMapper).updateItemTypeFromDto(updateDto, itemType);
        verify(itemTypeRepository).save(itemType);
        verify(itemTypeMapper).toDto(itemType);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(itemTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> itemTypeService.update(updateDto)
        );

        verify(itemTypeRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        itemTypeService.delete(itemType.getId());

        verify(itemTypeRepository).deleteById(itemType.getId());
    }
}
