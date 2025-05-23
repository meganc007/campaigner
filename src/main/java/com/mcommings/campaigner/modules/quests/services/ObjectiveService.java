package com.mcommings.campaigner.modules.quests.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.quests.dtos.ObjectiveDTO;
import com.mcommings.campaigner.modules.quests.entities.Objective;
import com.mcommings.campaigner.modules.quests.mappers.ObjectiveMapper;
import com.mcommings.campaigner.modules.quests.repositories.IObjectiveRepository;
import com.mcommings.campaigner.modules.quests.services.interfaces.IObjective;
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
public class ObjectiveService implements IObjective {

    private final IObjectiveRepository objectiveRepository;
    private final ObjectiveMapper objectiveMapper;

    @Override
    public List<ObjectiveDTO> getObjectives() {

        return objectiveRepository.findAll()
                .stream()
                .map(objectiveMapper::mapToObjectiveDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ObjectiveDTO> getObjective(int objectiveId) {
        return objectiveRepository.findById(objectiveId)
                .map(objectiveMapper::mapToObjectiveDto);
    }

    @Override
    public List<ObjectiveDTO> getObjectivesByCampaignUUID(UUID uuid) {
        return objectiveRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(objectiveMapper::mapToObjectiveDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ObjectiveDTO> getObjectivesWhereDescriptionContainsKeyword(String keyword) throws IllegalArgumentException {
        if (isNullOrEmpty(keyword)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return objectiveRepository.searchByDescription(keyword)
                .stream()
                .map(objectiveMapper::mapToObjectiveDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveObjective(ObjectiveDTO objective) throws IllegalArgumentException, DataIntegrityViolationException {
        if (isNullOrEmpty(objective.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (objectiveAlreadyExists(objectiveMapper.mapFromObjectiveDto(objective))) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        objectiveMapper.mapToObjectiveDto(
                objectiveRepository.save(objectiveMapper.mapFromObjectiveDto(objective))
        );
    }

    @Override
    public void deleteObjective(int objectiveId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(objectiveRepository, objectiveId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        objectiveRepository.deleteById(objectiveId);
    }

    @Override
    public Optional<ObjectiveDTO> updateObjective(int objectiveId, ObjectiveDTO objective) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(objectiveRepository, objectiveId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (isNullOrEmpty(objective.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return objectiveRepository.findById(objectiveId).map(foundObjective -> {
            if (objective.getDescription() != null) foundObjective.setDescription(objective.getDescription());
            if (objective.getNotes() != null) foundObjective.setNotes(objective.getNotes());

            return objectiveMapper.mapToObjectiveDto(objectiveRepository.save(foundObjective));
        });
    }

    private boolean isNullOrEmpty(String input) {
        return isNull(input) || input.isEmpty();
    }

    private boolean objectiveAlreadyExists(Objective objective) {
        return objectiveRepository.objectiveExists(objective).isPresent();
    }
}
