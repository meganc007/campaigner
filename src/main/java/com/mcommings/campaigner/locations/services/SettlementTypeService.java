package com.mcommings.campaigner.locations.services;

import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.locations.dtos.SettlementTypeDTO;
import com.mcommings.campaigner.locations.mappers.SettlementTypeMapper;
import com.mcommings.campaigner.locations.repositories.ISettlementTypeRepository;
import com.mcommings.campaigner.locations.services.interfaces.ISettlementType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class SettlementTypeService implements ISettlementType {

    private final ISettlementTypeRepository settlementTypeRepository;
    private final SettlementTypeMapper settlementTypeMapper;

    @Override
    public List<SettlementTypeDTO> getSettlementTypes() {

        return settlementTypeRepository.findAll()
                .stream()
                .map(settlementTypeMapper::mapToSettlementTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SettlementTypeDTO> getSettlementType(int settlementTypeId) {

        return settlementTypeRepository.findById(settlementTypeId)
                .map(settlementTypeMapper::mapToSettlementTypeDto);
    }

    @Override
    public void saveSettlementType(SettlementTypeDTO settlementType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(settlementType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(settlementTypeRepository, settlementType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        settlementTypeMapper.mapToSettlementTypeDto(
                settlementTypeRepository.save(
                        settlementTypeMapper.mapFromSettlementTypeDto(settlementType)
                ));
    }

    @Override
    public void deleteSettlementType(int settlementTypeId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(settlementTypeRepository, settlementTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        settlementTypeRepository.deleteById(settlementTypeId);
    }

    @Override
    public Optional<SettlementTypeDTO> updateSettlementType(int settlementTypeId, SettlementTypeDTO settlementType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(settlementTypeRepository, settlementTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(settlementType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(settlementTypeRepository, settlementType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return settlementTypeRepository.findById(settlementTypeId).map(foundSettlementType -> {
            if (settlementType.getName() != null) foundSettlementType.setName(settlementType.getName());
            if (settlementType.getDescription() != null)
                foundSettlementType.setDescription(settlementType.getDescription());

            return settlementTypeMapper.mapToSettlementTypeDto(settlementTypeRepository.save(foundSettlementType));
        });
    }
}
