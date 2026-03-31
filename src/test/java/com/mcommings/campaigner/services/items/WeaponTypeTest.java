package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.weapon_types.CreateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.UpdateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.ViewWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.entities.WeaponType;
import com.mcommings.campaigner.modules.items.mappers.WeaponTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IWeaponTypeRepository;
import com.mcommings.campaigner.modules.items.services.WeaponTypeService;
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
public class WeaponTypeTest {

    @Mock
    private IWeaponTypeRepository weaponTypeRepository;

    @Mock
    private WeaponTypeMapper weaponTypeMapper;

    @InjectMocks
    private WeaponTypeService weaponTypeService;

    private WeaponType weaponType;
    private ViewWeaponTypeDTO viewDto;
    private CreateWeaponTypeDTO createDto;
    private UpdateWeaponTypeDTO updateDto;

    @BeforeEach
    void setUp() {
        weaponType = ItemsTestDataFactory.weaponType();
        viewDto = ItemsTestDataFactory.viewWeaponTypeDTO();
        createDto = ItemsTestDataFactory.createWeaponTypeDTO();
        updateDto = ItemsTestDataFactory.updateWeaponTypeDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(weaponTypeRepository.findAll()).thenReturn(List.of(weaponType));
        when(weaponTypeMapper.toDto(weaponType)).thenReturn(viewDto);

        List<ViewWeaponTypeDTO> result = weaponTypeService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weaponTypeRepository).findAll();
        verify(weaponTypeMapper).toDto(weaponType);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(weaponTypeRepository.findById(weaponType.getId()))
                .thenReturn(Optional.of(weaponType));

        when(weaponTypeMapper.toDto(weaponType))
                .thenReturn(viewDto);

        ViewWeaponTypeDTO result = weaponTypeService.getById(weaponType.getId());

        assertEquals(viewDto, result);

        verify(weaponTypeRepository).findById(weaponType.getId());
        verify(weaponTypeMapper).toDto(weaponType);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(weaponTypeRepository.findById(weaponType.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> weaponTypeService.getById(weaponType.getId())
        );

        verify(weaponTypeRepository).findById(weaponType.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(weaponTypeMapper.toEntity(createDto)).thenReturn(weaponType);

        when(weaponTypeRepository.save(weaponType)).thenReturn(weaponType);
        when(weaponTypeMapper.toDto(weaponType)).thenReturn(viewDto);

        ViewWeaponTypeDTO result = weaponTypeService.create(createDto);

        assertEquals(viewDto, result);

        verify(weaponTypeMapper).toEntity(createDto);
        verify(weaponTypeRepository).save(weaponType);
        verify(weaponTypeMapper).toDto(weaponType);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(weaponTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(weaponType));

        when(weaponTypeRepository.save(weaponType)).thenReturn(weaponType);
        when(weaponTypeMapper.toDto(weaponType)).thenReturn(viewDto);

        ViewWeaponTypeDTO result = weaponTypeService.update(updateDto);

        assertEquals(viewDto, result);

        verify(weaponTypeRepository).findById(updateDto.getId());
        verify(weaponTypeMapper).updateWeaponTypeFromDto(updateDto, weaponType);
        verify(weaponTypeRepository).save(weaponType);
        verify(weaponTypeMapper).toDto(weaponType);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(weaponTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> weaponTypeService.update(updateDto)
        );

        verify(weaponTypeRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        weaponTypeService.delete(weaponType.getId());

        verify(weaponTypeRepository).deleteById(weaponType.getId());
    }
}
