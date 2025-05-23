package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.items.dtos.DamageTypeDTO;
import com.mcommings.campaigner.modules.items.mappers.DamageTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IDamageTypeRepository;
import com.mcommings.campaigner.modules.items.services.interfaces.IDamageType;
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
public class DamageTypeService implements IDamageType {

    private final IDamageTypeRepository damageTypeRepository;
    private final DamageTypeMapper damageTypeMapper;

    @Override
    public List<DamageTypeDTO> getDamageTypes() {

        return damageTypeRepository.findAll()
                .stream()
                .map(damageTypeMapper::mapToDamageTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DamageTypeDTO> getDamageType(int damageTypeId) {
        return damageTypeRepository.findById(damageTypeId)
                .map(damageTypeMapper::mapToDamageTypeDto);
    }

    @Override
    @Transactional
    public void saveDamageType(DamageTypeDTO damageType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(damageType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(damageTypeRepository, damageType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        damageTypeMapper.mapToDamageTypeDto(
                damageTypeRepository.save(damageTypeMapper.mapFromDamageTypeDto(damageType))
        );
    }

    @Override
    @Transactional
    public void deleteDamageType(int damageTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(damageTypeRepository, damageTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        damageTypeRepository.deleteById(damageTypeId);
    }

    @Override
    @Transactional
    public Optional<DamageTypeDTO> updateDamageType(int damageTypeId, DamageTypeDTO damageType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(damageTypeRepository, damageTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(damageType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(damageTypeRepository, damageType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return damageTypeRepository.findById(damageTypeId).map(foundDamageType -> {
            if (damageType.getName() != null) foundDamageType.setName(damageType.getName());
            if (damageType.getDescription() != null) foundDamageType.setDescription(damageType.getDescription());

            return damageTypeMapper.mapToDamageTypeDto(damageTypeRepository.save(foundDamageType));
        });
    }
}
