//package com.mcommings.campaigner.services.locations;
//
//import com.mcommings.campaigner.entities.RepositoryHelper;
//import com.mcommings.campaigner.locations.dtos.LandmarkDTO;
//import com.mcommings.campaigner.locations.entities.Landmark;
//import com.mcommings.campaigner.locations.services.interfaces.ILandmark;
//import com.mcommings.campaigner.locations.repositories.ILandmarkRepository;
//import com.mcommings.campaigner.repositories.locations.IRegionRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//import static com.mcommings.campaigner.enums.ErrorMessage.*;
//
//@Service
//public class LandmarkService implements ILandmark {
//
//    private final ILandmarkRepository landmarkRepository;
//
//    @Override
//    public List<LandmarkDTO> getLandmarks() {
//        return landmarkRepository.findAll();
//    }
//
//    @Override
//    public LandmarkDTO getLandmark(int landmarkId) {
//        return RepositoryHelper.getById(landmarkRepository, landmarkId);
//    }
//
//    @Override
//    public List<LandmarkDTO> getLandmarksByCampaignUUID(UUID uuid) {
//        return landmarkRepository.findByfk_campaign_uuid(uuid);
//    }
//
//    @Override
//    public List<LandmarkDTO> getLandmarksByRegionId(int regionId) {
//        return landmarkRepository.findByfk_region(regionId);
//    }
//
//    @Override
//    @Transactional
//    public void saveLandmark(LandmarkDTO landmark) throws IllegalArgumentException, DataIntegrityViolationException {
//        if (RepositoryHelper.nameIsNullOrEmpty(landmark)) {
//            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
//        }
//        if (RepositoryHelper.nameAlreadyExists(landmarkRepository, landmark)) {
//            throw new DataIntegrityViolationException(NAME_EXISTS.message);
//        }
//        if (hasForeignKeys(landmark) &&
//                RepositoryHelper.foreignKeyIsNotValid(landmarkRepository, getListOfForeignKeyRepositories(), landmark)) {
//            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
//        }
//
//        landmarkRepository.saveAndFlush(landmark);
//    }
//
//    @Override
//    @Transactional
//    public void deleteLandmark(int landmarkId) throws IllegalArgumentException, DataIntegrityViolationException {
//        if (RepositoryHelper.cannotFindId(landmarkRepository, landmarkId)) {
//            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
//        }
//
//        landmarkRepository.deleteById(landmarkId);
//    }
//
//    @Override
//    @Transactional
//    public void updateLandmark(int landmarkId, LandmarkDTO landmark)
//            throws IllegalArgumentException, DataIntegrityViolationException {
//        if (RepositoryHelper.cannotFindId(landmarkRepository, landmarkId)) {
//            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
//        }
//        if (hasForeignKeys(landmark) &&
//                RepositoryHelper.foreignKeyIsNotValid(landmarkRepository, getListOfForeignKeyRepositories(), landmark)) {
//            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
//        }
//        Landmark landmarkToUpdate = RepositoryHelper.getById(landmarkRepository, landmarkId);
//        if (landmark.getName() != null) landmarkToUpdate.setName(landmark.getName());
//        if (landmark.getDescription() != null) landmarkToUpdate.setDescription(landmark.getDescription());
//        if (landmark.getFk_region() != null) landmarkToUpdate.setFk_region(landmark.getFk_region());
//    }
//
//}
