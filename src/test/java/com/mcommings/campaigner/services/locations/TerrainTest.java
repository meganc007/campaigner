package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.locations.dtos.terrains.CreateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.UpdateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.ViewTerrainDTO;
import com.mcommings.campaigner.modules.locations.entities.Terrain;
import com.mcommings.campaigner.modules.locations.mappers.TerrainMapper;
import com.mcommings.campaigner.modules.locations.repositories.ITerrainRepository;
import com.mcommings.campaigner.modules.locations.services.TerrainService;
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
public class TerrainTest {

    @Mock
    private ITerrainRepository terrainRepository;

    @Mock
    private TerrainMapper terrainMapper;

    @InjectMocks
    private TerrainService terrainService;

    private Terrain terrain;
    private ViewTerrainDTO viewDto;
    private CreateTerrainDTO createDto;
    private UpdateTerrainDTO updateDto;

    @BeforeEach
    void setUp() {
        terrain = LocationsTestDataFactory.terrain();
        viewDto = LocationsTestDataFactory.viewTerrainDTO();
        createDto = LocationsTestDataFactory.createTerrainDTO();
        updateDto = LocationsTestDataFactory.updateTerrainDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(terrainRepository.findAll()).thenReturn(List.of(terrain));
        when(terrainMapper.toDto(terrain)).thenReturn(viewDto);

        List<ViewTerrainDTO> result = terrainService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(terrainRepository).findAll();
        verify(terrainMapper).toDto(terrain);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(terrainRepository.findById(terrain.getId()))
                .thenReturn(Optional.of(terrain));

        when(terrainMapper.toDto(terrain))
                .thenReturn(viewDto);

        ViewTerrainDTO result = terrainService.getById(terrain.getId());

        assertEquals(viewDto, result);

        verify(terrainRepository).findById(terrain.getId());
        verify(terrainMapper).toDto(terrain);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(terrainRepository.findById(terrain.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> terrainService.getById(terrain.getId())
        );

        verify(terrainRepository).findById(terrain.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(terrainMapper.toEntity(createDto)).thenReturn(terrain);

        when(terrainRepository.save(terrain)).thenReturn(terrain);
        when(terrainMapper.toDto(terrain)).thenReturn(viewDto);

        ViewTerrainDTO result = terrainService.create(createDto);

        assertEquals(viewDto, result);

        verify(terrainMapper).toEntity(createDto);
        verify(terrainRepository).save(terrain);
        verify(terrainMapper).toDto(terrain);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(terrainRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(terrain));

        when(terrainRepository.save(terrain)).thenReturn(terrain);
        when(terrainMapper.toDto(terrain)).thenReturn(viewDto);

        ViewTerrainDTO result = terrainService.update(updateDto);

        assertEquals(viewDto, result);

        verify(terrainRepository).findById(updateDto.getId());
        verify(terrainMapper).updateTerrainFromDto(updateDto, terrain);
        verify(terrainRepository).save(terrain);
        verify(terrainMapper).toDto(terrain);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(terrainRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> terrainService.update(updateDto)
        );

        verify(terrainRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        terrainService.delete(terrain.getId());

        verify(terrainRepository).deleteById(terrain.getId());
    }
}
