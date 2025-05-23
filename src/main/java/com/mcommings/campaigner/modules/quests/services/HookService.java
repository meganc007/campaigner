package com.mcommings.campaigner.modules.quests.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.quests.dtos.HookDTO;
import com.mcommings.campaigner.modules.quests.entities.Hook;
import com.mcommings.campaigner.modules.quests.mappers.HookMapper;
import com.mcommings.campaigner.modules.quests.repositories.IHookRepository;
import com.mcommings.campaigner.modules.quests.services.interfaces.IHook;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class HookService implements IHook {

    private final IHookRepository hookRepository;
    private final HookMapper hookMapper;

    @Override
    public List<HookDTO> getHooks() {
        return hookRepository.findAll()
                .stream()
                .map(hookMapper::mapToHookDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HookDTO> getHook(int hookId) {
        return hookRepository.findById(hookId)
                .map(hookMapper::mapToHookDto);
    }

    @Override
    public List<HookDTO> getHooksByCampaignUUID(UUID uuid) {

        return hookRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(hookMapper::mapToHookDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HookDTO> getHooksWhereDescriptionContainsKeyword(String keyword) throws IllegalArgumentException {
        if (isNullOrEmpty(keyword)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return hookRepository.searchByDescription(keyword)
                .stream()
                .map(hookMapper::mapToHookDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveHook(HookDTO hook) throws IllegalArgumentException, DataIntegrityViolationException {
        if (isNullOrEmpty(hook.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (hookAlreadyExists(hookMapper.mapFromHookDto(hook))) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        hookMapper.mapToHookDto(
                hookRepository.save(hookMapper.mapFromHookDto(hook)
                ));
    }

    @Override
    public void deleteHook(int hookId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(hookRepository, hookId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        hookRepository.deleteById(hookId);
    }

    @Override
    public Optional<HookDTO> updateHook(int hookId, HookDTO hook) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(hookRepository, hookId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (isNullOrEmpty(hook.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return hookRepository.findById(hookId).map(foundHook -> {
            if (hook.getDescription() != null) foundHook.setDescription(hook.getDescription());
            if (hook.getNotes() != null) foundHook.setNotes(hook.getNotes());

            return hookMapper.mapToHookDto(hookRepository.save(foundHook));
        });
    }

    private boolean isNullOrEmpty(String input) {
        return isNull(input) || input.isEmpty();
    }

    private boolean hookAlreadyExists(Hook hook) {
        return hookRepository.hookExists(hook).isPresent();
    }

}
