package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.damage_types.CreateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.UpdateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.ViewDamageTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.modules.items.mappers.DamageTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IDamageTypeRepository;
import com.mcommings.campaigner.modules.items.services.DamageTypeService;
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
public class DamageTypeTest {

    @Mock
    private IDamageTypeRepository damageTypeRepository;

    @Mock
    private DamageTypeMapper damageTypeMapper;

    @InjectMocks
    private DamageTypeService damageTypeService;

    private DamageType damageType;
    private ViewDamageTypeDTO viewDto;
    private CreateDamageTypeDTO createDto;
    private UpdateDamageTypeDTO updateDto;

    @BeforeEach
    void setUp() {
        damageType = ItemsTestDataFactory.damageType();
        viewDto = ItemsTestDataFactory.viewDamageTypeDTO();
        createDto = ItemsTestDataFactory.createDamageTypeDTO();
        updateDto = ItemsTestDataFactory.updateDamageTypeDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(damageTypeRepository.findAll()).thenReturn(List.of(damageType));
        when(damageTypeMapper.toDto(damageType)).thenReturn(viewDto);

        List<ViewDamageTypeDTO> result = damageTypeService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(damageTypeRepository).findAll();
        verify(damageTypeMapper).toDto(damageType);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(damageTypeRepository.findById(damageType.getId()))
                .thenReturn(Optional.of(damageType));

        when(damageTypeMapper.toDto(damageType))
                .thenReturn(viewDto);

        ViewDamageTypeDTO result = damageTypeService.getById(damageType.getId());

        assertEquals(viewDto, result);

        verify(damageTypeRepository).findById(damageType.getId());
        verify(damageTypeMapper).toDto(damageType);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(damageTypeRepository.findById(damageType.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> damageTypeService.getById(damageType.getId())
        );

        verify(damageTypeRepository).findById(damageType.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(damageTypeMapper.toEntity(createDto)).thenReturn(damageType);

        when(damageTypeRepository.save(damageType)).thenReturn(damageType);
        when(damageTypeMapper.toDto(damageType)).thenReturn(viewDto);

        ViewDamageTypeDTO result = damageTypeService.create(createDto);

        assertEquals(viewDto, result);

        verify(damageTypeMapper).toEntity(createDto);
        verify(damageTypeRepository).save(damageType);
        verify(damageTypeMapper).toDto(damageType);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(damageTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(damageType));

        when(damageTypeRepository.save(damageType)).thenReturn(damageType);
        when(damageTypeMapper.toDto(damageType)).thenReturn(viewDto);

        ViewDamageTypeDTO result = damageTypeService.update(updateDto);

        assertEquals(viewDto, result);

        verify(damageTypeRepository).findById(updateDto.getId());
        verify(damageTypeMapper).updateDamageTypeFromDto(updateDto, damageType);
        verify(damageTypeRepository).save(damageType);
        verify(damageTypeMapper).toDto(damageType);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(damageTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> damageTypeService.update(updateDto)
        );

        verify(damageTypeRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        damageTypeService.delete(damageType.getId());

        verify(damageTypeRepository).deleteById(damageType.getId());
    }
}
