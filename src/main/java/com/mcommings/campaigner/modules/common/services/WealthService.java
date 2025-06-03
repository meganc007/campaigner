package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.common.dtos.WealthDTO;
import com.mcommings.campaigner.modules.common.mappers.WealthMapper;
import com.mcommings.campaigner.modules.common.repositories.IWealthRepository;
import com.mcommings.campaigner.modules.common.services.interfaces.IWealth;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class WealthService implements IWealth {

    private final IWealthRepository wealthRepository;
    private final WealthMapper wealthMapper;

    @Override
    public List<WealthDTO> getWealth() {

        return wealthRepository.findAll().stream()
                .map(wealthMapper::mapToWealthDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveWealth(WealthDTO wealth) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(wealth)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(wealthRepository, wealth.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        wealthMapper.mapToWealthDto(
                wealthRepository.save(
                        wealthMapper.mapFromWealthDto(wealth))
        );
    }

    @Override
    public void deleteWealth(int wealthId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(wealthRepository, wealthId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        wealthRepository.deleteById(wealthId);
    }

    @Override
    public Optional<WealthDTO> updateWealth(int wealthId, WealthDTO wealth) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(wealthRepository, wealthId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(wealth)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExistsInAnotherRecord(wealthRepository, wealth.getName(), wealthId)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return wealthRepository.findById(wealthId).map(foundWealth -> {
            if (wealth.getName() != null) foundWealth.setName(wealth.getName());

            return wealthMapper.mapToWealthDto(wealthRepository.save(foundWealth));
        });
    }
}
