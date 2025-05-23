package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.modules.people.dtos.NamedMonsterDTO;
import com.mcommings.campaigner.modules.people.entities.NamedMonster;
import com.mcommings.campaigner.modules.people.mappers.NamedMonsterMapper;
import com.mcommings.campaigner.modules.people.repositories.INamedMonsterRepository;
import com.mcommings.campaigner.modules.people.services.NamedMonsterService;
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
public class NamedMonsterTest {

    @Mock
    private NamedMonsterMapper namedMonsterMapper;

    @Mock
    private INamedMonsterRepository namedMonsterRepository;

    @InjectMocks
    private NamedMonsterService namedMonsterService;

    private NamedMonster entity;
    private NamedMonsterDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new NamedMonster();
        entity.setId(1);
        entity.setFirstName("Monster");
        entity.setLastName("Mash");
        entity.setTitle("The biggest, baddest monster of them all.");
        entity.setFk_wealth(random.nextInt(100) + 1);
        entity.setFk_ability_score(random.nextInt(100) + 1);
        entity.setIsEnemy(false);
        entity.setPersonality("This is a personality");
        entity.setDescription("This is a description");
        entity.setNotes("This is a note");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new NamedMonsterDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setTitle(entity.getTitle());
        dto.setFk_wealth(entity.getFk_wealth());
        dto.setFk_ability_score(entity.getFk_ability_score());
        dto.setIsEnemy(entity.getIsEnemy());
        dto.setPersonality(entity.getPersonality());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(namedMonsterMapper.mapToNamedMonsterDto(entity)).thenReturn(dto);
        when(namedMonsterMapper.mapFromNamedMonsterDto(dto)).thenReturn(entity);
    }
    
    @Test
    public void whenThereAreNamedMonsters_getNamedMonsters_ReturnsNamedMonsters() {
        when(namedMonsterRepository.findAll()).thenReturn(List.of(entity));
        List<NamedMonsterDTO> result = namedMonsterService.getNamedMonsters();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Monster", result.get(0).getFirstName());
    }

    @Test
    public void whenThereAreNoNamedMonsters_getNamedMonsters_ReturnsNothing() {
        when(namedMonsterRepository.findAll()).thenReturn(Collections.emptyList());

        List<NamedMonsterDTO> result = namedMonsterService.getNamedMonsters();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no namedMonsters.");
    }

    @Test
    public void whenThereIsANamedMonster_getNamedMonster_ReturnsNamedMonsters() {
        when(namedMonsterRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<NamedMonsterDTO> result = namedMonsterService.getNamedMonster(1);

        assertTrue(result.isPresent());
        assertEquals("Mash", result.get().getLastName());
    }

    @Test
    public void whenThereIsNotANamedMonster_getNamedMonster_ReturnsNamedMonster() {
        when(namedMonsterRepository.findById(999)).thenReturn(Optional.empty());

        Optional<NamedMonsterDTO> result = namedMonsterService.getNamedMonster(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when namedMonster is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getNamedMonstersByCampaignUUID_ReturnsNamedMonsters() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(namedMonsterRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<NamedMonsterDTO> result = namedMonsterService.getNamedMonstersByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getNamedMonstersByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(namedMonsterRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<NamedMonsterDTO> result = namedMonsterService.getNamedMonstersByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no namedMonsters match the campaign UUID.");
    }

    @Test
    public void whenNamedMonsterIsValid_saveNamedMonster_SavesTheNamedMonster() {
        when(namedMonsterRepository.save(entity)).thenReturn(entity);

        namedMonsterService.saveNamedMonster(dto);

        verify(namedMonsterRepository, times(1)).save(entity);
    }

    @Test
    public void whenNamedMonsterNameIsInvalid_saveNamedMonster_ThrowsIllegalArgumentException() {
        NamedMonsterDTO namedMonsterWithEmptyName = new NamedMonsterDTO();
        namedMonsterWithEmptyName.setId(1);
        namedMonsterWithEmptyName.setFirstName("");
        namedMonsterWithEmptyName.setDescription("A fictional namedMonster.");
        namedMonsterWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());

        NamedMonsterDTO namedMonsterWithNullName = new NamedMonsterDTO();
        namedMonsterWithNullName.setId(1);
        namedMonsterWithNullName.setFirstName(null);
        namedMonsterWithNullName.setDescription("A fictional city.");
        namedMonsterWithNullName.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.saveNamedMonster(namedMonsterWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.saveNamedMonster(namedMonsterWithNullName));
    }

    @Test
    public void whenNamedMonsterNameAlreadyExists_saveNamedMonster_ThrowsDataIntegrityViolationException() {
        when(namedMonsterMapper.mapFromNamedMonsterDto(dto)).thenReturn(entity);
        when(namedMonsterRepository.monsterExists(any(NamedMonster.class))).thenReturn(Optional.of(entity));

        assertThrows(DataIntegrityViolationException.class, () -> namedMonsterService.saveNamedMonster(dto));
        verify(namedMonsterRepository, times(1)).monsterExists(namedMonsterMapper.mapFromNamedMonsterDto(dto));
        verify(namedMonsterRepository, never()).save(any(NamedMonster.class));
    }

    @Test
    public void whenNamedMonsterIdExists_deleteNamedMonster_DeletesTheNamedMonster() {
        when(namedMonsterRepository.existsById(1)).thenReturn(true);
        namedMonsterService.deleteNamedMonster(1);
        verify(namedMonsterRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenNamedMonsterIdDoesNotExist_deleteNamedMonster_ThrowsIllegalArgumentException() {
        when(namedMonsterRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.deleteNamedMonster(999));
    }

    @Test
    public void whenDeleteNamedMonsterFails_deleteNamedMonster_ThrowsException() {
        when(namedMonsterRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(namedMonsterRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> namedMonsterService.deleteNamedMonster(1));
    }

    @Test
    public void whenNamedMonsterIdIsFound_updateNamedMonster_UpdatesTheNamedMonster() {
        NamedMonsterDTO updateDTO = new NamedMonsterDTO();
        updateDTO.setFirstName("John");

        when(namedMonsterRepository.findById(1)).thenReturn(Optional.of(entity));
        when(namedMonsterRepository.existsById(1)).thenReturn(true);
        when(namedMonsterRepository.save(entity)).thenReturn(entity);
        when(namedMonsterMapper.mapToNamedMonsterDto(entity)).thenReturn(updateDTO);

        Optional<NamedMonsterDTO> result = namedMonsterService.updateNamedMonster(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    public void whenNamedMonsterIdIsNotFound_updateNamedMonster_ReturnsEmptyOptional() {
        NamedMonsterDTO updateDTO = new NamedMonsterDTO();
        updateDTO.setFirstName("John");

        when(namedMonsterRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.updateNamedMonster(999, updateDTO));
    }

    @Test
    public void whenNamedMonsterNameIsInvalid_updateNamedMonster_ThrowsIllegalArgumentException() {
        NamedMonsterDTO updateEmptyName = new NamedMonsterDTO();
        updateEmptyName.setFirstName("");

        NamedMonsterDTO updateNullName = new NamedMonsterDTO();
        updateNullName.setFirstName(null);

        when(namedMonsterRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.updateNamedMonster(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> namedMonsterService.updateNamedMonster(1, updateNullName));
    }

    @Test
    public void whenNamedMonsterNameAlreadyExists_updateNamedMonster_ThrowsDataIntegrityViolationException() {
        when(namedMonsterRepository.existsById(dto.getId())).thenReturn(true);
        when(namedMonsterMapper.mapFromNamedMonsterDto(dto)).thenReturn(entity);
        when(namedMonsterRepository.findByFirstNameAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.of(entity));

        assertThrows(DataIntegrityViolationException.class, () -> namedMonsterService.updateNamedMonster(dto.getId(), dto));

        verify(namedMonsterRepository, times(1)).existsById(dto.getId());
        verify(namedMonsterRepository, times(1)).findByFirstNameAndLastName(dto.getFirstName(), dto.getLastName());
        verify(namedMonsterRepository, never()).save(any(NamedMonster.class));
    }
}
