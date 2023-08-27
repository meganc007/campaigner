package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ILandmark;
import com.mcommings.campaigner.models.Landmark;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.ILandmarkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class LandmarkService implements ILandmark {

    private final ILandmarkRepository landmarkRepository;

    @Autowired
    public LandmarkService(ILandmarkRepository landmarkRepository) {
        this.landmarkRepository = landmarkRepository;
    }

    @Override
    public List<Landmark> getLandmarks() {
        return landmarkRepository.findAll();
    }

    @Override
    @Transactional
    public void saveLandmark(Landmark landmark) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(landmark)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(landmarkRepository, landmark)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        landmarkRepository.saveAndFlush(landmark);
    }

    @Override
    @Transactional
    public void deleteLandmark(int landmarkId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(landmarkRepository, landmarkId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if foreign key

        landmarkRepository.deleteById(landmarkId);
    }

    @Override
    @Transactional
    public void updateLandmark(int landmarkId, Landmark landmark)
            throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(landmarkRepository, landmarkId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Landmark landmarkToUpdate = RepositoryHelper.getById(landmarkRepository, landmarkId);
        landmarkToUpdate.setName(landmark.getName());
        landmarkToUpdate.setDescription(landmark.getDescription());
    }
}
