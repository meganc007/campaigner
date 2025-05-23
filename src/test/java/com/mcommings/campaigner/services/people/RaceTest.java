package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.modules.people.dtos.RaceDTO;
import com.mcommings.campaigner.modules.people.entities.Race;
import com.mcommings.campaigner.modules.people.mappers.RaceMapper;
import com.mcommings.campaigner.modules.people.repositories.IRaceRepository;
import com.mcommings.campaigner.modules.people.services.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RaceTest {

    @Mock
    private RaceMapper raceMapper;

    @Mock
    private IRaceRepository raceRepository;

    @InjectMocks
    private RaceService raceService;

    private Race entity;
    private RaceDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Race();
        entity.setId(1);
        entity.setName("Test Race");
        entity.setDescription("A description.");
        entity.setIsExotic(false);

        dto = new RaceDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setIsExotic(entity.getIsExotic());

        when(raceMapper.mapToRaceDto(entity)).thenReturn(dto);
        when(raceMapper.mapFromRaceDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreRaces_getRaces_ReturnsRaces() {
        when(raceRepository.findAll()).thenReturn(List.of(entity));
        List<RaceDTO> result = raceService.getRaces();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Race", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoRaces_getRaces_ReturnsNothing() {
        when(raceRepository.findAll()).thenReturn(Collections.emptyList());

        List<RaceDTO> result = raceService.getRaces();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no races.");
    }

    @Test
    public void whenThereIsARace_getRace_ReturnsRace() {
        when(raceRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<RaceDTO> result = raceService.getRace(1);

        assertTrue(result.isPresent());
        assertEquals("Test Race", result.get().getName());
    }

    @Test
    public void whenThereIsNotARace_getRace_ReturnsRace() {
        when(raceRepository.findById(999)).thenReturn(Optional.empty());

        Optional<RaceDTO> result = raceService.getRace(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when race is not found.");
    }

    @Test
    public void whenRaceIsValid_saveRace_SavesTheRace() {
        when(raceRepository.save(entity)).thenReturn(entity);

        raceService.saveRace(dto);

        verify(raceRepository, times(1)).save(entity);
    }

    @Test
    public void whenRaceNameIsInvalid_saveRace_ThrowsIllegalArgumentException() {
        RaceDTO raceWithEmptyName = new RaceDTO();
        raceWithEmptyName.setId(1);
        raceWithEmptyName.setName("");
        raceWithEmptyName.setDescription("A fictional race.");
        raceWithEmptyName.setIsExotic(true);

        RaceDTO raceWithNullName = new RaceDTO();
        raceWithNullName.setId(1);
        raceWithNullName.setName(null);
        raceWithNullName.setDescription("A fictional city.");
        raceWithNullName.setIsExotic(false);

        assertThrows(IllegalArgumentException.class, () -> raceService.saveRace(raceWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> raceService.saveRace(raceWithNullName));
    }

    @Test
    public void whenRaceNameAlreadyExists_saveRace_ThrowsDataIntegrityViolationException() {
        when(raceRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> raceService.saveRace(dto));
        verify(raceRepository, times(1)).findByName(dto.getName());
        verify(raceRepository, never()).save(any(Race.class));
    }

    @Test
    public void whenRaceIdExists_deleteRace_DeletesTheRace() {
        when(raceRepository.existsById(1)).thenReturn(true);
        raceService.deleteRace(1);
        verify(raceRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenRaceIdDoesNotExist_deleteRace_ThrowsIllegalArgumentException() {
        when(raceRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> raceService.deleteRace(999));
    }

    @Test
    public void whenDeleteRaceFails_deleteRace_ThrowsException() {
        when(raceRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(raceRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> raceService.deleteRace(1));
    }

    @Test
    public void whenRaceIdIsFound_updateRace_UpdatesTheRace() {
        RaceDTO updateDTO = new RaceDTO();
        updateDTO.setName("Updated Name");

        when(raceRepository.findById(1)).thenReturn(Optional.of(entity));
        when(raceRepository.existsById(1)).thenReturn(true);
        when(raceRepository.save(entity)).thenReturn(entity);
        when(raceMapper.mapToRaceDto(entity)).thenReturn(updateDTO);

        Optional<RaceDTO> result = raceService.updateRace(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    public void whenRaceIdIsNotFound_updateRace_ReturnsEmptyOptional() {
        RaceDTO updateDTO = new RaceDTO();
        updateDTO.setName("Updated Name");

        when(raceRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> raceService.updateRace(999, updateDTO));
    }

    @Test
    public void whenRaceNameIsInvalid_updateRace_ThrowsIllegalArgumentException() {
        RaceDTO updateEmptyName = new RaceDTO();
        updateEmptyName.setName("");

        RaceDTO updateNullName = new RaceDTO();
        updateNullName.setName(null);

        when(raceRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> raceService.updateRace(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> raceService.updateRace(1, updateNullName));
    }

    @Test
    public void whenRaceNameAlreadyExists_updateRace_ThrowsDataIntegrityViolationException() {
        when(raceRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> raceService.updateRace(entity.getId(), dto));
    }
}
