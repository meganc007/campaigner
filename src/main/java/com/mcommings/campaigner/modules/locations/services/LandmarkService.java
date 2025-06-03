package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.locations.dtos.LandmarkDTO;
import com.mcommings.campaigner.modules.locations.mappers.LandmarkMapper;
import com.mcommings.campaigner.modules.locations.repositories.ILandmarkRepository;
import com.mcommings.campaigner.modules.locations.services.interfaces.ILandmark;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class LandmarkService implements ILandmark {

    private final ILandmarkRepository landmarkRepository;
    private final LandmarkMapper landmarkMapper;

    @Override
    public List<LandmarkDTO> getLandmarks() {
        return landmarkRepository.findAll()
                .stream()
                .map(landmarkMapper::mapToLandmarkDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LandmarkDTO> getLandmark(int landmarkId) {
        return landmarkRepository.findById(landmarkId)
                .map(landmarkMapper::mapToLandmarkDto);
    }

    @Override
    public List<LandmarkDTO> getLandmarksByCampaignUUID(UUID uuid) {
        return landmarkRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(landmarkMapper::mapToLandmarkDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LandmarkDTO> getLandmarksByRegionId(int regionId) {
        return landmarkRepository.findByfk_region(regionId)
                .stream()
                .map(landmarkMapper::mapToLandmarkDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveLandmark(LandmarkDTO landmark) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(landmark)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(landmarkRepository, landmark.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        landmarkMapper.mapToLandmarkDto(
                landmarkRepository.save(landmarkMapper.mapFromLandmarkDto(landmark))
        );
    }

    @Override
    public void deleteLandmark(int landmarkId) throws IllegalArgumentException {
        if (RepositoryHelper.cannotFindId(landmarkRepository, landmarkId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        landmarkRepository.deleteById(landmarkId);
    }

    @Override
    public Optional<LandmarkDTO> updateLandmark(int landmarkId, LandmarkDTO landmark) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(landmarkRepository, landmarkId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(landmark)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExistsInAnotherRecord(landmarkRepository, landmark.getName(), landmarkId)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return landmarkRepository.findById(landmarkId).map(foundLandmark -> {
            if (landmark.getName() != null) foundLandmark.setName(landmark.getName());
            if (landmark.getDescription() != null) foundLandmark.setDescription(landmark.getDescription());
            if (landmark.getFk_campaign_uuid() != null)
                foundLandmark.setFk_campaign_uuid(landmark.getFk_campaign_uuid());
            if (landmark.getFk_region() != null) foundLandmark.setFk_region(landmark.getFk_region());

            return landmarkMapper.mapToLandmarkDto(landmarkRepository.save(foundLandmark));
        });
    }
}
