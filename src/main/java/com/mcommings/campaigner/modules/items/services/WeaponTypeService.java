package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.items.dtos.WeaponTypeDTO;
import com.mcommings.campaigner.modules.items.mappers.WeaponTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IWeaponTypeRepository;
import com.mcommings.campaigner.modules.items.services.interfaces.IWeaponType;
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
public class WeaponTypeService implements IWeaponType {

    private final IWeaponTypeRepository weaponTypeRepository;
    private final WeaponTypeMapper weaponTypeMapper;

    @Override
    public List<WeaponTypeDTO> getWeaponTypes() {

        return weaponTypeRepository.findAll()
                .stream()
                .map(weaponTypeMapper::mapToWeaponTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WeaponTypeDTO> getWeaponType(int weaponTypeId) {
        return weaponTypeRepository.findById(weaponTypeId)
                .map(weaponTypeMapper::mapToWeaponTypeDto);
    }

    @Override
    @Transactional
    public void saveWeaponType(WeaponTypeDTO weaponType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(weaponType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(weaponTypeRepository, weaponType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        weaponTypeMapper.mapToWeaponTypeDto(
                weaponTypeRepository.save(weaponTypeMapper.mapFromWeaponTypeDto(weaponType))
        );
    }

    @Override
    @Transactional
    public void deleteWeaponType(int weaponTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weaponTypeRepository, weaponTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        weaponTypeRepository.deleteById(weaponTypeId);
    }

    @Override
    @Transactional
    public Optional<WeaponTypeDTO> updateWeaponType(int weaponTypeId, WeaponTypeDTO weaponType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weaponTypeRepository, weaponTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(weaponType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExistsInAnotherRecord(weaponTypeRepository, weaponType.getName(), weaponTypeId)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return weaponTypeRepository.findById(weaponTypeId).map(foundWeaponType -> {
            if (weaponType.getName() != null) foundWeaponType.setName(weaponType.getName());
            if (weaponType.getDescription() != null) foundWeaponType.setDescription(weaponType.getDescription());

            return weaponTypeMapper.mapToWeaponTypeDto(weaponTypeRepository.save(foundWeaponType));
        });
    }
}
