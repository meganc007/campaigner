package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.modules.quests.dtos.HookDTO;
import com.mcommings.campaigner.modules.quests.entities.Hook;
import com.mcommings.campaigner.modules.quests.mappers.HookMapper;
import com.mcommings.campaigner.modules.quests.repositories.IHookRepository;
import com.mcommings.campaigner.modules.quests.services.HookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HookTest {

    @Mock
    private HookMapper hookMapper;

    @Mock
    private IHookRepository hookRepository;

    @InjectMocks
    private HookService hookService;

    private Hook entity;
    private HookDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Hook();
        entity.setId(1);
        entity.setDescription("This is a description.");
        entity.setNotes("This is a note.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new HookDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(hookMapper.mapToHookDto(entity)).thenReturn(dto);
        when(hookMapper.mapFromHookDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreHooks_getHooks_ReturnsHooks() {
        when(hookRepository.findAll()).thenReturn(List.of(entity));
        List<HookDTO> result = hookService.getHooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("This is a description.", result.get(0).getDescription());
    }

    @Test
    public void whenThereAreNoHooks_getHooks_ReturnsNothing() {
        when(hookRepository.findAll()).thenReturn(Collections.emptyList());

        List<HookDTO> result = hookService.getHooks();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no hooks.");
    }

    @Test
    public void whenThereIsAHook_getHook_ReturnsHook() {
        when(hookRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<HookDTO> result = hookService.getHook(1);

        assertTrue(result.isPresent());
        assertEquals("This is a description.", result.get().getDescription());
    }

    @Test
    public void whenThereIsNotAHook_getHook_ReturnsHook() {
        when(hookRepository.findById(999)).thenReturn(Optional.empty());

        Optional<HookDTO> result = hookService.getHook(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when hook is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getHooksByCampaignUUID_ReturnsHooks() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(hookRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<HookDTO> result = hookService.getHooksByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getHooksByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(hookRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<HookDTO> result = hookService.getHooksByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no hooks match the campaign UUID.");
    }

    @Test
    public void whenHookIsValid_saveHook_SavesTheHook() {
        when(hookRepository.save(entity)).thenReturn(entity);

        hookService.saveHook(dto);

        verify(hookRepository, times(1)).save(entity);
    }

    @Test
    public void whenHookDescriptionIsInvalid_saveHook_ThrowsIllegalArgumentException() {
        HookDTO hookWithEmptyDescription = new HookDTO();
        hookWithEmptyDescription.setId(1);
        hookWithEmptyDescription.setDescription("");
        hookWithEmptyDescription.setFk_campaign_uuid(UUID.randomUUID());

        HookDTO hookWithNullDescription = new HookDTO();
        hookWithNullDescription.setId(1);
        hookWithNullDescription.setDescription(null);
        hookWithNullDescription.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> hookService.saveHook(hookWithEmptyDescription));
        assertThrows(IllegalArgumentException.class, () -> hookService.saveHook(hookWithNullDescription));
    }

    @Test
    public void whenHookDescriptionAlreadyExists_saveHook_ThrowsDataIntegrityViolationException() {
        when(hookRepository.hookExists(hookMapper.mapFromHookDto(dto))).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> hookService.saveHook(dto));
        verify(hookRepository, times(1)).hookExists(hookMapper.mapFromHookDto(dto));
        verify(hookRepository, never()).save(any(Hook.class));
    }

    @Test
    public void whenHookIdExists_deleteHook_DeletesTheHook() {
        when(hookRepository.existsById(1)).thenReturn(true);
        hookService.deleteHook(1);
        verify(hookRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenHookIdDoesNotExist_deleteHook_ThrowsIllegalArgumentException() {
        when(hookRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> hookService.deleteHook(999));
    }

    @Test
    public void whenDeleteHookFails_deleteHook_ThrowsException() {
        when(hookRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(hookRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> hookService.deleteHook(1));
    }

    @Test
    public void whenHookIdIsFound_updateHook_UpdatesTheHook() {
        HookDTO updateDTO = new HookDTO();
        updateDTO.setDescription("Updated description");

        when(hookRepository.findById(1)).thenReturn(Optional.of(entity));
        when(hookRepository.existsById(1)).thenReturn(true);
        when(hookRepository.save(entity)).thenReturn(entity);
        when(hookMapper.mapToHookDto(entity)).thenReturn(updateDTO);

        Optional<HookDTO> result = hookService.updateHook(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated description", result.get().getDescription());
    }

    @Test
    public void whenHookIdIsNotFound_updateHook_ReturnsEmptyOptional() {
        HookDTO updateDTO = new HookDTO();
        updateDTO.setDescription("Updated");

        when(hookRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> hookService.updateHook(999, updateDTO));
    }

    @Test
    public void whenHookDescriptionIsInvalid_updateHook_ThrowsIllegalArgumentException() {
        HookDTO updateEmptyDescription = new HookDTO();
        updateEmptyDescription.setDescription("");

        HookDTO updateNullDescription = new HookDTO();
        updateNullDescription.setDescription(null);

        when(hookRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> hookService.updateHook(1, updateEmptyDescription));
        assertThrows(IllegalArgumentException.class, () -> hookService.updateHook(1, updateNullDescription));
    }

    @Test
    public void whenHookNameAlreadyExists_updateHook_ThrowsDataIntegrityViolationException() {
        when(hookRepository.hookExists(hookMapper.mapFromHookDto(dto))).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> hookService.updateHook(entity.getId(), dto));
    }
    
}
