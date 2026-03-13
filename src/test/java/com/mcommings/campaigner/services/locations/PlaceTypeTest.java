package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.locations.dtos.place_types.CreatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.UpdatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.ViewPlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.PlaceType;
import com.mcommings.campaigner.modules.locations.mappers.PlaceTypeMapper;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceTypesRepository;
import com.mcommings.campaigner.modules.locations.services.PlaceTypeService;
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
public class PlaceTypeTest {

    @Mock
    private IPlaceTypesRepository placeTypeRepository;

    @Mock
    private PlaceTypeMapper placeTypeMapper;

    @InjectMocks
    private PlaceTypeService placeTypeService;

    private PlaceType placeType;
    private ViewPlaceTypeDTO viewDto;
    private CreatePlaceTypeDTO createDto;
    private UpdatePlaceTypeDTO updateDto;

    @BeforeEach
    void setUp() {
        placeType = LocationsTestDataFactory.placeType();
        viewDto = LocationsTestDataFactory.viewPlaceTypeDTO();
        createDto = LocationsTestDataFactory.createPlaceTypeDTO();
        updateDto = LocationsTestDataFactory.updatePlaceTypeDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(placeTypeRepository.findAll()).thenReturn(List.of(placeType));
        when(placeTypeMapper.toDto(placeType)).thenReturn(viewDto);

        List<ViewPlaceTypeDTO> result = placeTypeService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(placeTypeRepository).findAll();
        verify(placeTypeMapper).toDto(placeType);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(placeTypeRepository.findById(placeType.getId()))
                .thenReturn(Optional.of(placeType));

        when(placeTypeMapper.toDto(placeType))
                .thenReturn(viewDto);

        ViewPlaceTypeDTO result = placeTypeService.getById(placeType.getId());

        assertEquals(viewDto, result);

        verify(placeTypeRepository).findById(placeType.getId());
        verify(placeTypeMapper).toDto(placeType);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(placeTypeRepository.findById(placeType.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> placeTypeService.getById(placeType.getId())
        );

        verify(placeTypeRepository).findById(placeType.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(placeTypeMapper.toEntity(createDto)).thenReturn(placeType);

        when(placeTypeRepository.save(placeType)).thenReturn(placeType);
        when(placeTypeMapper.toDto(placeType)).thenReturn(viewDto);

        ViewPlaceTypeDTO result = placeTypeService.create(createDto);

        assertEquals(viewDto, result);

        verify(placeTypeMapper).toEntity(createDto);
        verify(placeTypeRepository).save(placeType);
        verify(placeTypeMapper).toDto(placeType);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(placeTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(placeType));

        when(placeTypeRepository.save(placeType)).thenReturn(placeType);
        when(placeTypeMapper.toDto(placeType)).thenReturn(viewDto);

        ViewPlaceTypeDTO result = placeTypeService.update(updateDto);

        assertEquals(viewDto, result);

        verify(placeTypeRepository).findById(updateDto.getId());
        verify(placeTypeMapper).updatePlaceTypeFromDto(updateDto, placeType);
        verify(placeTypeRepository).save(placeType);
        verify(placeTypeMapper).toDto(placeType);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(placeTypeRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> placeTypeService.update(updateDto)
        );

        verify(placeTypeRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        placeTypeService.delete(placeType.getId());

        verify(placeTypeRepository).deleteById(placeType.getId());
    }
}
