package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.quests.Hook;
import com.mcommings.campaigner.interfaces.quests.IHook;
import com.mcommings.campaigner.repositories.quests.IHookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
public class HookService implements IHook {

    private final IHookRepository hookRepository;

    @Autowired
    public HookService(IHookRepository hookRepository) {
        this.hookRepository = hookRepository;
    }

    @Override
    public List<Hook> getHooks() {
        return hookRepository.findAll();
    }

    @Override
    public List<Hook> getHooksByCampaignUUID(UUID uuid) {
        return hookRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    @Transactional
    public void saveHook(Hook hook) throws IllegalArgumentException, DataIntegrityViolationException {
        if (descriptionIsNullOrEmpty(hook.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (hookAlreadyExists(hook)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        hookRepository.saveAndFlush(hook);
    }

    @Override
    @Transactional
    public void deleteHook(int hookId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(hookRepository, hookId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
//        TODO: uncomment when Hook is used as a fk
//        if (RepositoryHelper.isForeignKey(getReposWhereHookIsAForeignKey(), FK_HOOK.columnName, hookId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        hookRepository.deleteById(hookId);
    }

    @Override
    @Transactional
    public void updateHook(int hookId, Hook hook) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(hookRepository, hookId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Hook hookToUpdate = RepositoryHelper.getById(hookRepository, hookId);
        if (hook.getDescription() != null) hookToUpdate.setDescription(hook.getDescription());
        if (hook.getNotes() != null) hookToUpdate.setNotes(hook.getNotes());
    }

    private boolean descriptionIsNullOrEmpty(String description) {
        return isNull(description) || description.isEmpty();
    }

    private boolean hookAlreadyExists(Hook hook) {
        return hookRepository.hookExists(hook).isPresent();
    }

//    TODO: fix once Hook is used as fk
//    private List<CrudRepository> getReposWhereHookIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }
}
