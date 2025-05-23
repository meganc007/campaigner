package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.common.dtos.GovernmentDTO;
import com.mcommings.campaigner.modules.common.mappers.GovernmentMapper;
import com.mcommings.campaigner.modules.common.repositories.IGovernmentRepository;
import com.mcommings.campaigner.modules.common.services.interfaces.IGovernment;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class GovernmentService implements IGovernment {

    private final IGovernmentRepository governmentRepository;
    private final GovernmentMapper governmentMapper;

    @Override
    public List<GovernmentDTO> getGovernments() {

        return governmentRepository.findAll()
                .stream()
                .map(governmentMapper::mapToGovernmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GovernmentDTO> getGovernment(int governmentId) {

        return governmentRepository.findById(governmentId)
                .map(governmentMapper::mapToGovernmentDto);
    }

    @Override
    public void saveGovernment(GovernmentDTO government) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(government)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(governmentRepository, government.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        governmentMapper.mapToGovernmentDto(
                governmentRepository.save(
                        governmentMapper.mapFromGovernmentDto(government))
        );
    }

    @Override
    public void deleteGovernment(int governmentId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(governmentRepository, governmentId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        governmentRepository.deleteById(governmentId);

    }

    @Override
    public Optional<GovernmentDTO> updateGovernment(int governmentId, GovernmentDTO government) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(governmentRepository, governmentId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(government)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(governmentRepository, government.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return governmentRepository.findById(governmentId).map(foundGovernment -> {
            if (government.getName() != null) foundGovernment.setName(government.getName());
            if (government.getDescription() != null) foundGovernment.setDescription(government.getDescription());

            return governmentMapper.mapToGovernmentDto(governmentRepository.save(foundGovernment));
        });
        
    }
}
