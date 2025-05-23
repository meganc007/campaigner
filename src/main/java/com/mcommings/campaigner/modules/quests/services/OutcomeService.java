package com.mcommings.campaigner.modules.quests.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.quests.dtos.OutcomeDTO;
import com.mcommings.campaigner.modules.quests.entities.Outcome;
import com.mcommings.campaigner.modules.quests.mappers.OutcomeMapper;
import com.mcommings.campaigner.modules.quests.repositories.IOutcomeRepository;
import com.mcommings.campaigner.modules.quests.services.interfaces.IOutcome;
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
public class OutcomeService implements IOutcome {

    private final IOutcomeRepository outcomeRepository;
    private final OutcomeMapper outcomeMapper;

    @Override
    public List<OutcomeDTO> getOutcomes() {
        return outcomeRepository.findAll()
                .stream()
                .map(outcomeMapper::mapToOutcomeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OutcomeDTO> getOutcome(int outcomeId) {
        return outcomeRepository.findById(outcomeId)
                .map(outcomeMapper::mapToOutcomeDto);
    }

    @Override
    public List<OutcomeDTO> getOutcomesByCampaignUUID(UUID uuid) {

        return outcomeRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(outcomeMapper::mapToOutcomeDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OutcomeDTO> getOutcomesWhereDescriptionContainsKeyword(String keyword) throws IllegalArgumentException {
        if (isNullOrEmpty(keyword)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return outcomeRepository.searchByDescription(keyword)
                .stream()
                .map(outcomeMapper::mapToOutcomeDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveOutcome(OutcomeDTO outcome) throws IllegalArgumentException, DataIntegrityViolationException {
        if (isNullOrEmpty(outcome.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (outcomeAlreadyExists(outcomeMapper.mapFromOutcomeDto(outcome))) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        outcomeMapper.mapToOutcomeDto(
                outcomeRepository.save(outcomeMapper.mapFromOutcomeDto(outcome)
                ));
    }

    @Override
    public void deleteOutcome(int outcomeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(outcomeRepository, outcomeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        outcomeRepository.deleteById(outcomeId);
    }

    @Override
    public Optional<OutcomeDTO> updateOutcome(int outcomeId, OutcomeDTO outcome) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(outcomeRepository, outcomeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (isNullOrEmpty(outcome.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return outcomeRepository.findById(outcomeId).map(foundOutcome -> {
            if (outcome.getDescription() != null) foundOutcome.setDescription(outcome.getDescription());
            if (outcome.getNotes() != null) foundOutcome.setNotes(outcome.getNotes());

            return outcomeMapper.mapToOutcomeDto(outcomeRepository.save(foundOutcome));
        });
    }

    private boolean isNullOrEmpty(String input) {
        return isNull(input) || input.isEmpty();
    }

    private boolean outcomeAlreadyExists(Outcome outcome) {
        return outcomeRepository.outcomeExists(outcome).isPresent();
    }
}
