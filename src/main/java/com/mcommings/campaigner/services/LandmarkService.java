package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.ILandmark;
import com.mcommings.campaigner.models.Landmark;
import com.mcommings.campaigner.models.repositories.ILandmarkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void saveLandmark(Landmark landmark) {

    }

    @Override
    @Transactional
    public void deleteLandmark(int landmarkId) {

    }

    @Override
    @Transactional
    public void updateLandmark(int landmarkId, Landmark landmark) {

    }
}
