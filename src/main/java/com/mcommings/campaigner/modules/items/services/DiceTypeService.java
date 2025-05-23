package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.items.dtos.DiceTypeDTO;
import com.mcommings.campaigner.modules.items.mappers.DiceTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IDiceTypeRepository;
import com.mcommings.campaigner.modules.items.services.interfaces.IDiceType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class DiceTypeService implements IDiceType {

    private final IDiceTypeRepository diceTypeRepository;
    private final DiceTypeMapper diceTypeMapper;

    @Override
    public List<DiceTypeDTO> getDiceTypes() {

        return diceTypeRepository.findAll()
                .stream()
                .map(diceTypeMapper::mapToDiceTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DiceTypeDTO> getDiceType(int diceTypeId) {
        return diceTypeRepository.findById(diceTypeId)
                .map(diceTypeMapper::mapToDiceTypeDto);
    }

    @Override
    @Transactional
    public void saveDiceType(DiceTypeDTO diceType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(diceType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(diceTypeRepository, diceType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        diceTypeMapper.mapToDiceTypeDto(
                diceTypeRepository.save(diceTypeMapper.mapFromDiceTypeDto(diceType))
        );
    }

    @Override
    @Transactional
    public void deleteDiceType(int diceTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(diceTypeRepository, diceTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        diceTypeRepository.deleteById(diceTypeId);
    }

    @Override
    @Transactional
    public Optional<DiceTypeDTO> updateDiceType(int diceTypeId, DiceTypeDTO diceType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(diceTypeRepository, diceTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(diceType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(diceTypeRepository, diceType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return diceTypeRepository.findById(diceTypeId).map(foundDiceType -> {
            if (diceType.getName() != null) foundDiceType.setName(diceType.getName());
            if (diceType.getDescription() != null) foundDiceType.setDescription(diceType.getDescription());
            if (diceType.getMax_roll() >= 0) foundDiceType.setMax_roll(diceType.getMax_roll());

            return diceTypeMapper.mapToDiceTypeDto(diceTypeRepository.save(foundDiceType));
        });
    }
}
