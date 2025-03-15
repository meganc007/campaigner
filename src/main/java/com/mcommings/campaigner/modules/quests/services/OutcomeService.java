package com.mcommings.campaigner.modules.quests.services;

import com.mcommings.campaigner.modules.common.entities.RepositoryHelper;
import com.mcommings.campaigner.modules.quests.entities.Outcome;
import com.mcommings.campaigner.modules.quests.repositories.IOutcomeRepository;
import com.mcommings.campaigner.modules.quests.services.interfaces.IOutcome;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
public class OutcomeService implements IOutcome {

    private final IOutcomeRepository outcomeRepository;

    @Autowired
    public OutcomeService(IOutcomeRepository outcomeRepository) {
        this.outcomeRepository = outcomeRepository;
    }

    @Override
    public List<Outcome> getOutcomes() {
        return outcomeRepository.findAll();
    }

    @Override
    public List<Outcome> getOutcomesByCampaignUUID(UUID uuid) {
        return outcomeRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    @Transactional
    public void saveOutcome(Outcome outcome) throws IllegalArgumentException, DataIntegrityViolationException {
        if (descriptionIsNullOrEmpty(outcome.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (outcomeAlreadyExists(outcome)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        outcomeRepository.saveAndFlush(outcome);
    }

    @Override
    @Transactional
    public void deleteOutcome(int outcomeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(outcomeRepository, outcomeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
//        TODO: uncomment when Outcome is used as a fk
//        if (RepositoryHelper.isForeignKey(getReposWhereOutcomeIsAForeignKey(), FK_OUTCOME.columnName, outcomeId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        outcomeRepository.deleteById(outcomeId);
    }

    @Override
    @Transactional
    public void updateOutcome(int outcomeId, Outcome outcome) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(outcomeRepository, outcomeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Outcome outcomeToUpdate = RepositoryHelper.getById(outcomeRepository, outcomeId);
        if (outcome.getDescription() != null) outcomeToUpdate.setDescription(outcome.getDescription());
        if (outcome.getNotes() != null) outcomeToUpdate.setNotes(outcome.getNotes());
    }

    private boolean descriptionIsNullOrEmpty(String description) {
        return isNull(description) || description.isEmpty();
    }

    private boolean outcomeAlreadyExists(Outcome outcome) {
        return outcomeRepository.outcomeExists(outcome).isPresent();
    }

//    TODO: fix once Outcome is used as fk
//    private List<CrudRepository> getReposWhereOutcomeIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }
}
