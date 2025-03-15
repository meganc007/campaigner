package com.mcommings.campaigner.items.services;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.items.entities.WeaponType;
import com.mcommings.campaigner.items.repositories.IWeaponRepository;
import com.mcommings.campaigner.items.repositories.IWeaponTypeRepository;
import com.mcommings.campaigner.items.services.interfaces.IWeaponType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_WEAPON_TYPE;

@Service
public class WeaponTypeService implements IWeaponType {

    private final IWeaponTypeRepository weaponTypeRepository;
    private final IWeaponRepository weaponRepository;

    @Autowired
    public WeaponTypeService(IWeaponTypeRepository weaponTypeRepository, IWeaponRepository weaponRepository) {
        this.weaponTypeRepository = weaponTypeRepository;
        this.weaponRepository = weaponRepository;
    }

    @Override
    public List<WeaponType> getWeaponTypes() {
        return weaponTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void saveWeaponType(WeaponType weaponType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(weaponType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(weaponTypeRepository, weaponType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        weaponTypeRepository.saveAndFlush(weaponType);
    }

    @Override
    @Transactional
    public void deleteWeaponType(int weaponTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weaponTypeRepository, weaponTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereWeaponTypeIsAForeignKey(), FK_WEAPON_TYPE.columnName, weaponTypeId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        weaponTypeRepository.deleteById(weaponTypeId);
    }

    @Override
    @Transactional
    public void updateWeaponType(int weaponTypeId, WeaponType weaponType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weaponTypeRepository, weaponTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        WeaponType weaponTypeToUpdate = RepositoryHelper.getById(weaponTypeRepository, weaponTypeId);
        if (weaponType.getName() != null) weaponTypeToUpdate.setName(weaponType.getName());
        if (weaponType.getDescription() != null) weaponTypeToUpdate.setDescription(weaponType.getDescription());
    }

    private List<CrudRepository> getReposWhereWeaponTypeIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(weaponRepository));
    }
}
