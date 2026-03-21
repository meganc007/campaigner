package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.climate.CreateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.UpdateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.ViewClimateDTO;
import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.modules.common.mappers.ClimateMapper;
import com.mcommings.campaigner.modules.common.repositories.IClimateRepository;
import com.mcommings.campaigner.modules.common.services.ClimateService;
import com.mcommings.campaigner.setup.common.factories.CommonTestDataFactory;
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
public class ClimateTest {

    @Mock
    private IClimateRepository climateRepository;

    @Mock
    private ClimateMapper climateMapper;

    @InjectMocks
    private ClimateService climateService;

    private Climate climate;
    private ViewClimateDTO viewDto;
    private CreateClimateDTO createDto;
    private UpdateClimateDTO updateDto;

    @BeforeEach
    void setUp() {
        climate = CommonTestDataFactory.climate();
        viewDto = CommonTestDataFactory.viewClimateDTO();
        createDto = CommonTestDataFactory.createClimateDTO();
        updateDto = CommonTestDataFactory.updateClimateDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(climateRepository.findAll()).thenReturn(List.of(climate));
        when(climateMapper.toDto(climate)).thenReturn(viewDto);

        List<ViewClimateDTO> result = climateService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(climateRepository).findAll();
        verify(climateMapper).toDto(climate);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(climateRepository.findById(climate.getId()))
                .thenReturn(Optional.of(climate));

        when(climateMapper.toDto(climate))
                .thenReturn(viewDto);

        ViewClimateDTO result = climateService.getById(climate.getId());

        assertEquals(viewDto, result);

        verify(climateRepository).findById(climate.getId());
        verify(climateMapper).toDto(climate);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(climateRepository.findById(climate.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> climateService.getById(climate.getId())
        );

        verify(climateRepository).findById(climate.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(climateMapper.toEntity(createDto)).thenReturn(climate);

        when(climateRepository.save(climate)).thenReturn(climate);
        when(climateMapper.toDto(climate)).thenReturn(viewDto);

        ViewClimateDTO result = climateService.create(createDto);

        assertEquals(viewDto, result);

        verify(climateMapper).toEntity(createDto);
        verify(climateRepository).save(climate);
        verify(climateMapper).toDto(climate);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(climateRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(climate));

        when(climateRepository.save(climate)).thenReturn(climate);
        when(climateMapper.toDto(climate)).thenReturn(viewDto);

        ViewClimateDTO result = climateService.update(updateDto);

        assertEquals(viewDto, result);

        verify(climateRepository).findById(updateDto.getId());
        verify(climateMapper).updateClimateFromDto(updateDto, climate);
        verify(climateRepository).save(climate);
        verify(climateMapper).toDto(climate);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(climateRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> climateService.update(updateDto)
        );

        verify(climateRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        climateService.delete(climate.getId());

        verify(climateRepository).deleteById(climate.getId());
    }
}
