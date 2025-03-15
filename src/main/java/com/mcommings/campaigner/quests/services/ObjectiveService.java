package com.mcommings.campaigner.quests.services;

import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.quests.entities.Objective;
import com.mcommings.campaigner.quests.repositories.IObjectiveRepository;
import com.mcommings.campaigner.quests.services.interfaces.IObjective;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
public class ObjectiveService implements IObjective {

    private final IObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveService(IObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    @Override
    public List<Objective> getObjectives() {
        return objectiveRepository.findAll();
    }

    @Override
    public List<Objective> getObjectivesByCampaignUUID(UUID uuid) {
        return objectiveRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    @Transactional
    public void saveObjective(Objective objective) throws IllegalArgumentException, DataIntegrityViolationException {
        if (descriptionIsNullOrEmpty(objective.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (objectiveAlreadyExists(objective)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        objectiveRepository.saveAndFlush(objective);
    }

    @Override
    @Transactional
    public void deleteObjective(int objectiveId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(objectiveRepository, objectiveId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
//        TODO: uncomment when Objective is used as a fk
//        if (RepositoryHelper.isForeignKey(getReposWhereObjectiveIsAForeignKey(), FK_OBJECTIVE.columnName, objectiveId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        objectiveRepository.deleteById(objectiveId);
    }

    @Override
    @Transactional
    public void updateObjective(int objectiveId, Objective objective) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(objectiveRepository, objectiveId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Objective objectiveToUpdate = RepositoryHelper.getById(objectiveRepository, objectiveId);
        if (objective.getDescription() != null) objectiveToUpdate.setDescription(objective.getDescription());
        if (objective.getNotes() != null) objectiveToUpdate.setNotes(objective.getNotes());
    }

    private boolean descriptionIsNullOrEmpty(String description) {
        return isNull(description) || description.isEmpty();
    }

    private boolean objectiveAlreadyExists(Objective objective) {
        return objectiveRepository.objectiveExists(objective).isPresent();
    }

//    TODO: fix once Objective is used as fk
//    private List<CrudRepository> getReposWhereObjectiveIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }
}
