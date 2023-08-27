package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IGovernment;
import com.mcommings.campaigner.models.Government;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.IGovernmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class GovernmentService implements IGovernment {

    private final IGovernmentRepository governmentRepository;

    @Autowired
    public GovernmentService (IGovernmentRepository governmentRepository) {this.governmentRepository = governmentRepository;}

    @Override
    public List<Government> getGovernments() {
        return governmentRepository.findAll();
    }

    @Override
    @Transactional
    public void saveGovernment(Government government) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.nameIsNullOrEmpty(government)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if(RepositoryHelper.nameAlreadyExists(governmentRepository, government)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        governmentRepository.saveAndFlush(government);
    }

    @Override
    @Transactional
    public void deleteGovernment(int governmentId) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(governmentRepository, governmentId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if government id is a foreign key

        governmentRepository.deleteById(governmentId);

    }

    @Override
    @Transactional
    public void updateGovernment(int governmentId, Government government) throws IllegalArgumentException, DataIntegrityViolationException {
        if(RepositoryHelper.cannotFindId(governmentRepository, governmentId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Government governmentToUpdate = RepositoryHelper.getById(governmentRepository, governmentId);
        governmentToUpdate.setName(government.getName());
        governmentToUpdate.setDescription(government.getDescription());
    }
}
