package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.quests.entities.Hook;
import com.mcommings.campaigner.quests.repositories.IHookRepository;
import com.mcommings.campaigner.quests.services.HookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HookTest {

    @Mock
    private IHookRepository hookRepository;

    @InjectMocks
    private HookService hookService;

    @Test
    public void whenThereAreHooks_getHooks_ReturnsHooks() {
        List<Hook> hooks = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        hooks.add(new Hook(1, "Hook 1", "Notes 1", campaign));
        hooks.add(new Hook(2, "Hook 2", "Notes 2", campaign));
        hooks.add(new Hook(2, "Hook 3", campaign));

        when(hookRepository.findAll()).thenReturn(hooks);

        List<Hook> result = hookService.getHooks();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(hooks, result);
    }

    @Test
    public void whenThereAreNoHooks_getHooks_ReturnsNothing() {
        List<Hook> hooks = new ArrayList<>();
        when(hookRepository.findAll()).thenReturn(hooks);

        List<Hook> result = hookService.getHooks();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(hooks, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getHooksByCampaignUUID_ReturnsHooks() {
        List<Hook> hooks = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        hooks.add(new Hook(1, "Hook 1", "Notes 1", campaign));
        hooks.add(new Hook(2, "Hook 2", "Notes 2", campaign));
        hooks.add(new Hook(2, "Hook 3", campaign));

        when(hookRepository.findByfk_campaign_uuid(campaign)).thenReturn(hooks);

        List<Hook> results = hookService.getHooksByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(hooks, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getHooksByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Hook> hooks = new ArrayList<>();
        when(hookRepository.findByfk_campaign_uuid(campaign)).thenReturn(hooks);

        List<Hook> result = hookService.getHooksByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(hooks, result);
    }

    @Test
    public void whenHookIsValid_saveHook_SavesTheHook() {
        Hook hook = new Hook(1, "Hook 1", "Notes 1", UUID.randomUUID());
        when(hookRepository.saveAndFlush(hook)).thenReturn(hook);

        assertDoesNotThrow(() -> hookService.saveHook(hook));
        verify(hookRepository, times(1)).saveAndFlush(hook);
    }

    @Test
    public void whenHookNameIsInvalid_saveHook_ThrowsIllegalArgumentException() {
        Hook hookWithEmptyName = new Hook(1, "", "Notes 1", UUID.randomUUID());
        Hook hookWithNullName = new Hook(2, null, "Notes 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> hookService.saveHook(hookWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> hookService.saveHook(hookWithNullName));
    }

    @Test
    public void whenHookDescriptionAlreadyExists_saveHook_ThrowsDataIntegrityViolationException() {
        Hook hook = new Hook(1, "Hook 1", "Note 1", UUID.randomUUID());
        Hook hookWithDuplicatedName = new Hook(2, "Hook 1", "Note 2", UUID.randomUUID());
        when(hookRepository.saveAndFlush(hook)).thenReturn(hook);
        when(hookRepository.saveAndFlush(hookWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> hookService.saveHook(hook));
        assertThrows(DataIntegrityViolationException.class, () -> hookService.saveHook(hookWithDuplicatedName));
    }

    @Test
    public void whenHookIdExists_deleteHook_DeletesTheHook() {
        int hookId = 1;
        when(hookRepository.existsById(hookId)).thenReturn(true);
        assertDoesNotThrow(() -> hookService.deleteHook(hookId));
        verify(hookRepository, times(1)).deleteById(hookId);
    }

    @Test
    public void whenHookIdDoesNotExist_deleteHook_ThrowsIllegalArgumentException() {
        int hookId = 9000;
        when(hookRepository.existsById(hookId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> hookService.deleteHook(hookId));
    }

    // TODO: fix when Hook is used as a fk
//    @Test
//    public void whenHookIdIsAForeignKey_deleteHook_ThrowsDataIntegrityViolationException() {
//        int hookId = 1;
//        Region region = new Region(1, "Region", "Description", 1, hookId);
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(regionRepository));
//        List<Region> regions = new ArrayList<>(Arrays.asList(region));
//
//        when(hookRepository.existsById(hookId)).thenReturn(true);
//        when(regionRepository.findByfk_hook(hookId)).thenReturn(regions);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_HOOK.columnName, hookId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> hookService.deleteHook(hookId));
//    }

    @Test
    public void whenHookIdIsFound_updateHook_UpdatesTheHook() {
        int hookId = 1;
        UUID campaign = UUID.randomUUID();
        Hook hook = new Hook(hookId, "Old Hook Name", "Old Notes", campaign);
        Hook hookToUpdate = new Hook(hookId, "Updated Hook Name", "Updated Notes", campaign);

        when(hookRepository.existsById(hookId)).thenReturn(true);
        when(hookRepository.findById(hookId)).thenReturn(Optional.of(hook));

        hookService.updateHook(hookId, hookToUpdate);

        verify(hookRepository).findById(hookId);

        Hook result = hookRepository.findById(hookId).get();
        Assertions.assertEquals(hookToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(hookToUpdate.getNotes(), result.getNotes());
    }

    @Test
    public void whenHookIdIsNotFound_updateHook_ThrowsIllegalArgumentException() {
        int hookId = 1;
        Hook hook = new Hook(hookId, "Old Hook Name", "Old Description", UUID.randomUUID());

        when(hookRepository.existsById(hookId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> hookService.updateHook(hookId, hook));
    }

    @Test
    public void whenSomeHookFieldsChanged_updateHook_OnlyUpdatesChangedFields() {
        int hookId = 1;
        Hook hook = new Hook(hookId, "Name", "Description", UUID.randomUUID());

        String newDescription = "New Hook description";

        Hook hookToUpdate = new Hook();
        hookToUpdate.setDescription(newDescription);

        when(hookRepository.existsById(hookId)).thenReturn(true);
        when(hookRepository.findById(hookId)).thenReturn(Optional.of(hook));

        hookService.updateHook(hookId, hookToUpdate);

        verify(hookRepository).findById(hookId);

        Hook result = hookRepository.findById(hookId).get();
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(hook.getNotes(), result.getNotes());
    }
}
