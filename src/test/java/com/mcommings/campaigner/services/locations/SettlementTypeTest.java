package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.locations.dtos.settlement_types.CreateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.UpdateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.ViewSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import com.mcommings.campaigner.modules.locations.mappers.SettlementTypeMapper;
import com.mcommings.campaigner.modules.locations.repositories.ISettlementTypeRepository;
import com.mcommings.campaigner.modules.locations.services.SettlementTypeService;
import com.mcommings.campaigner.setup.locations.factories.LocationsTestDataFactory;
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
public class SettlementTypeTest {

    @Mock
    private ISettlementTypeRepository settlementTypeRepository;

    @Mock
    private SettlementTypeMapper settlementTypeMapper;

    @InjectMocks
    private SettlementTypeService settlementTypeService;

    private SettlementType settlementType;
    private ViewSettlementTypeDTO viewDto;
    private CreateSettlementTypeDTO createDto;
    private UpdateSettlementTypeDTO updateDto;

    @BeforeEach
    void setUp() {
        settlementType = LocationsTestDataFactory.settlementType();
        viewDto = LocationsTestDataFactory.viewSettlementTypeDTO();
        createDto = LocationsTestDataFactory.createSettlementTypeDTO();
        updateDto = LocationsTestDataFactory.updateSettlementTypeDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(settlementTypeRepository.findAll()).thenReturn(List.of(settlementType));
        when(settlementTypeMapper.toDto(settlementType)).thenReturn(viewDto);

        List<ViewSettlementTypeDTO> result = settlementTypeService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(settlementTypeRepository).findAll();
        verify(settlementTypeMapper).toDto(settlementType);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(settlementTypeRepository.findById(settlementType.getId()))
                .thenReturn(Optional.of(settlementType));

        when(settlementTypeMapper.toDto(settlementType))
                .thenReturn(viewDto);

        ViewSettlementTypeDTO result = settlementTypeService.getById(settlementType.getId());

        assertEquals(viewDto, result);

        verify(settlementTypeRepository).findById(settlementType.getId());
        verify(settlementTypeMapper).toDto(settlementType);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(settlementTypeRepository.findById(settlementType.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> settlementTypeService.getById(settlementType.getId())
        );

        verify(settlementTypeRepository).findById(settlementType.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(settlementTypeMapper.toEntity(createDto)).thenReturn(settlementType);

        when(settlementTypeRepository.save(settlementType)).thenReturn(settlementType);
        when(settlementTypeMapper.toDto(settlementType)).thenReturn(viewDto);

        ViewSettlementTypeDTO result = settlementTypeService.create(createDto);

        assertEquals(viewDto, result);

        verify(settlementTypeMapper).toEntity(createDto);
        verify(settlementTypeRepository).save(settlementType);
        verify(settlementTypeMapper).toDto(settlementType);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(settlementTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(settlementType));

        when(settlementTypeRepository.save(settlementType)).thenReturn(settlementType);
        when(settlementTypeMapper.toDto(settlementType)).thenReturn(viewDto);

        ViewSettlementTypeDTO result = settlementTypeService.update(updateDto);

        assertEquals(viewDto, result);

        verify(settlementTypeRepository).findById(updateDto.getId());
        verify(settlementTypeMapper).updateSettlementTypeFromDto(updateDto, settlementType);
        verify(settlementTypeRepository).save(settlementType);
        verify(settlementTypeMapper).toDto(settlementType);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(settlementTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> settlementTypeService.update(updateDto)
        );

        verify(settlementTypeRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        settlementTypeService.delete(settlementType.getId());

        verify(settlementTypeRepository).deleteById(settlementType.getId());
    }
}
