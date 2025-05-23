package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.modules.people.dtos.GenericMonsterDTO;
import com.mcommings.campaigner.modules.people.entities.GenericMonster;
import com.mcommings.campaigner.modules.people.mappers.GenericMonsterMapper;
import com.mcommings.campaigner.modules.people.repositories.IGenericMonsterRepository;
import com.mcommings.campaigner.modules.people.services.GenericMonsterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GenericMonsterTest {

    @Mock
    private GenericMonsterMapper genericMonsterMapper;

    @Mock
    private IGenericMonsterRepository genericMonsterRepository;

    @InjectMocks
    private GenericMonsterService genericMonsterService;

    private GenericMonster entity;
    private GenericMonsterDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new GenericMonster();
        entity.setId(1);
        entity.setName("Test Generic Monster");
        entity.setDescription("A generic monster.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setTraits("This is a trait.");
        entity.setNotes("This is a note.");
        entity.setFk_ability_score(random.nextInt(100) + 1);

        dto = new GenericMonsterDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setTraits(entity.getTraits());
        dto.setNotes(entity.getNotes());
        dto.setFk_ability_score(entity.getFk_ability_score());

        when(genericMonsterMapper.mapToGenericMonsterDto(entity)).thenReturn(dto);
        when(genericMonsterMapper.mapFromGenericMonsterDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreGenericMonsters_getGenericMonsters_ReturnsGenericMonsters() {
        when(genericMonsterRepository.findAll()).thenReturn(List.of(entity));
        List<GenericMonsterDTO> result = genericMonsterService.getGenericMonsters();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Generic Monster", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoGenericMonsters_getGenericMonsters_ReturnsNothing() {
        when(genericMonsterRepository.findAll()).thenReturn(Collections.emptyList());

        List<GenericMonsterDTO> result = genericMonsterService.getGenericMonsters();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no genericMonsters.");
    }

    @Test
    public void whenThereIsAGenericMonster_getGenericMonster_ReturnsGenericMonster() {
        when(genericMonsterRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<GenericMonsterDTO> result = genericMonsterService.getGenericMonster(1);

        assertTrue(result.isPresent());
        assertEquals("Test Generic Monster", result.get().getName());
    }

    @Test
    public void whenThereIsNotAGenericMonster_getGenericMonster_ReturnsGenericMonster() {
        when(genericMonsterRepository.findById(999)).thenReturn(Optional.empty());

        Optional<GenericMonsterDTO> result = genericMonsterService.getGenericMonster(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when genericMonster is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getGenericMonstersByCampaignUUID_ReturnsGenericMonsters() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(genericMonsterRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<GenericMonsterDTO> result = genericMonsterService.getGenericMonstersByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getGenericMonstersByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(genericMonsterRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<GenericMonsterDTO> result = genericMonsterService.getGenericMonstersByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no genericMonsters match the campaign UUID.");
    }

    @Test
    public void whenGenericMonsterIsValid_saveGenericMonster_SavesTheGenericMonster() {
        when(genericMonsterRepository.save(entity)).thenReturn(entity);

        genericMonsterService.saveGenericMonster(dto);

        verify(genericMonsterRepository, times(1)).save(entity);
    }

    @Test
    public void whenGenericMonsterNameIsInvalid_saveGenericMonster_ThrowsIllegalArgumentException() {
        GenericMonsterDTO genericMonsterWithEmptyName = new GenericMonsterDTO();
        genericMonsterWithEmptyName.setId(1);
        genericMonsterWithEmptyName.setName("");
        genericMonsterWithEmptyName.setDescription("A fictional genericMonster.");
        genericMonsterWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());

        GenericMonsterDTO genericMonsterWithNullName = new GenericMonsterDTO();
        genericMonsterWithNullName.setId(1);
        genericMonsterWithNullName.setName(null);
        genericMonsterWithNullName.setDescription("A fictional city.");
        genericMonsterWithNullName.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.saveGenericMonster(genericMonsterWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.saveGenericMonster(genericMonsterWithNullName));
    }

    @Test
    public void whenGenericMonsterNameAlreadyExists_saveGenericMonster_ThrowsDataIntegrityViolationException() {
        when(genericMonsterRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> genericMonsterService.saveGenericMonster(dto));
        verify(genericMonsterRepository, times(1)).findByName(dto.getName());
        verify(genericMonsterRepository, never()).save(any(GenericMonster.class));
    }

    @Test
    public void whenGenericMonsterIdExists_deleteGenericMonster_DeletesTheGenericMonster() {
        when(genericMonsterRepository.existsById(1)).thenReturn(true);
        genericMonsterService.deleteGenericMonster(1);
        verify(genericMonsterRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenGenericMonsterIdDoesNotExist_deleteGenericMonster_ThrowsIllegalArgumentException() {
        when(genericMonsterRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.deleteGenericMonster(999));
    }

    @Test
    public void whenDeleteGenericMonsterFails_deleteGenericMonster_ThrowsException() {
        when(genericMonsterRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(genericMonsterRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> genericMonsterService.deleteGenericMonster(1));
    }

    @Test
    public void whenGenericMonsterIdIsFound_updateGenericMonster_UpdatesTheGenericMonster() {
        GenericMonsterDTO updateDTO = new GenericMonsterDTO();
        updateDTO.setName("Updated Name");

        when(genericMonsterRepository.findById(1)).thenReturn(Optional.of(entity));
        when(genericMonsterRepository.existsById(1)).thenReturn(true);
        when(genericMonsterRepository.save(entity)).thenReturn(entity);
        when(genericMonsterMapper.mapToGenericMonsterDto(entity)).thenReturn(updateDTO);

        Optional<GenericMonsterDTO> result = genericMonsterService.updateGenericMonster(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    public void whenGenericMonsterIdIsNotFound_updateGenericMonster_ReturnsEmptyOptional() {
        GenericMonsterDTO updateDTO = new GenericMonsterDTO();
        updateDTO.setName("Updated Name");

        when(genericMonsterRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.updateGenericMonster(999, updateDTO));
    }

    @Test
    public void whenGenericMonsterNameIsInvalid_updateGenericMonster_ThrowsIllegalArgumentException() {
        GenericMonsterDTO updateEmptyName = new GenericMonsterDTO();
        updateEmptyName.setName("");

        GenericMonsterDTO updateNullName = new GenericMonsterDTO();
        updateNullName.setName(null);

        when(genericMonsterRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.updateGenericMonster(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.updateGenericMonster(1, updateNullName));
    }

    @Test
    public void whenGenericMonsterNameAlreadyExists_updateGenericMonster_ThrowsDataIntegrityViolationException() {
        when(genericMonsterRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> genericMonsterService.updateGenericMonster(entity.getId(), dto));
    }
}
