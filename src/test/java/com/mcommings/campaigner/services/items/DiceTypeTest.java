package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.dice_types.CreateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.UpdateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.ViewDiceTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DiceType;
import com.mcommings.campaigner.modules.items.mappers.DiceTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IDiceTypeRepository;
import com.mcommings.campaigner.modules.items.services.DiceTypeService;
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
public class DiceTypeTest {

    @Mock
    private IDiceTypeRepository diceTypeRepository;

    @Mock
    private DiceTypeMapper diceTypeMapper;

    @InjectMocks
    private DiceTypeService diceTypeService;

    private DiceType diceType;
    private ViewDiceTypeDTO viewDto;
    private CreateDiceTypeDTO createDto;
    private UpdateDiceTypeDTO updateDto;

    @BeforeEach
    void setUp() {
        diceType = ItemsTestDataFactory.diceType();
        viewDto = ItemsTestDataFactory.viewDiceTypeDTO();
        createDto = ItemsTestDataFactory.createDiceTypeDTO();
        updateDto = ItemsTestDataFactory.updateDiceTypeDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(diceTypeRepository.findAll()).thenReturn(List.of(diceType));
        when(diceTypeMapper.toDto(diceType)).thenReturn(viewDto);

        List<ViewDiceTypeDTO> result = diceTypeService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(diceTypeRepository).findAll();
        verify(diceTypeMapper).toDto(diceType);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(diceTypeRepository.findById(diceType.getId()))
                .thenReturn(Optional.of(diceType));

        when(diceTypeMapper.toDto(diceType))
                .thenReturn(viewDto);

        ViewDiceTypeDTO result = diceTypeService.getById(diceType.getId());

        assertEquals(viewDto, result);

        verify(diceTypeRepository).findById(diceType.getId());
        verify(diceTypeMapper).toDto(diceType);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(diceTypeRepository.findById(diceType.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> diceTypeService.getById(diceType.getId())
        );

        verify(diceTypeRepository).findById(diceType.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(diceTypeMapper.toEntity(createDto)).thenReturn(diceType);

        when(diceTypeRepository.save(diceType)).thenReturn(diceType);
        when(diceTypeMapper.toDto(diceType)).thenReturn(viewDto);

        ViewDiceTypeDTO result = diceTypeService.create(createDto);

        assertEquals(viewDto, result);

        verify(diceTypeMapper).toEntity(createDto);
        verify(diceTypeRepository).save(diceType);
        verify(diceTypeMapper).toDto(diceType);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(diceTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(diceType));

        when(diceTypeRepository.save(diceType)).thenReturn(diceType);
        when(diceTypeMapper.toDto(diceType)).thenReturn(viewDto);

        ViewDiceTypeDTO result = diceTypeService.update(updateDto);

        assertEquals(viewDto, result);

        verify(diceTypeRepository).findById(updateDto.getId());
        verify(diceTypeMapper).updateDiceTypeFromDto(updateDto, diceType);
        verify(diceTypeRepository).save(diceType);
        verify(diceTypeMapper).toDto(diceType);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(diceTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> diceTypeService.update(updateDto)
        );

        verify(diceTypeRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        diceTypeService.delete(diceType.getId());

        verify(diceTypeRepository).deleteById(diceType.getId());
    }
}
