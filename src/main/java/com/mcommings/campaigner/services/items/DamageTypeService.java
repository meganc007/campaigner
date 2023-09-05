package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.interfaces.items.IDamageType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.DamageType;
import com.mcommings.campaigner.repositories.items.IDamageTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class DamageTypeService implements IDamageType {

    private final IDamageTypeRepository damageTypeRepository;

    @Autowired
    public DamageTypeService(IDamageTypeRepository damageTypeRepository) {
        this.damageTypeRepository = damageTypeRepository;
    }

    @Override
    public List<DamageType> getDamageTypes() {
        return damageTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void saveDamageType(DamageType damageType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(damageType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(damageTypeRepository, damageType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        damageTypeRepository.saveAndFlush(damageType);
    }

    @Override
    @Transactional
    public void deleteDamageType(int damageTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(damageTypeRepository, damageTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
// TODO: uncomment when class that uses DamageType as a fk is added
//        if (RepositoryHelper.isForeignKey(getReposWhereDamageTypeIsAForeignKey(), FK_DAMAGE_TYPE.columnName, damageTypeId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        damageTypeRepository.deleteById(damageTypeId);
    }

    @Override
    @Transactional
    public void updateDamageType(int damageTypeId, DamageType damageType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(damageTypeRepository, damageTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        DamageType damageTypeToUpdate = RepositoryHelper.getById(damageTypeRepository, damageTypeId);
        damageTypeToUpdate.setName(damageType.getName());
        damageTypeToUpdate.setDescription(damageType.getDescription());
    }

// TODO: uncomment when class that uses DamageType as a fk is added
//    private List<CrudRepository> getReposWhereDamageTypeIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }
}
