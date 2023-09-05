package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.interfaces.items.IDiceType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.DiceType;
import com.mcommings.campaigner.repositories.items.IDiceTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class DiceTypeService implements IDiceType {

    private final IDiceTypeRepository diceTypeRepository;

    @Autowired
    public DiceTypeService(IDiceTypeRepository diceTypeRepository) {
        this.diceTypeRepository = diceTypeRepository;
    }

    @Override
    public List<DiceType> getDiceTypes() {
        return diceTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void saveDiceType(DiceType diceType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(diceType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(diceTypeRepository, diceType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        diceTypeRepository.saveAndFlush(diceType);
    }

    @Override
    @Transactional
    public void deleteDiceType(int diceTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(diceTypeRepository, diceTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
// TODO: uncomment when class that uses DiceType as a fk is added
//        if (RepositoryHelper.isForeignKey(getReposWhereDiceTypeIsAForeignKey(), FK_DICE_TYPE.columnName, diceTypeId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        diceTypeRepository.deleteById(diceTypeId);
    }

    @Override
    @Transactional
    public void updateDiceType(int diceTypeId, DiceType diceType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(diceTypeRepository, diceTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        DiceType diceTypeToUpdate = RepositoryHelper.getById(diceTypeRepository, diceTypeId);
        diceTypeToUpdate.setName(diceType.getName());
        diceTypeToUpdate.setDescription(diceType.getDescription());
        diceTypeToUpdate.setMax_roll(diceType.getMax_roll());
    }

// TODO: uncomment when class that uses DiceType as a fk is added
//    private List<CrudRepository> getReposWhereDiceTypeIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }
}
