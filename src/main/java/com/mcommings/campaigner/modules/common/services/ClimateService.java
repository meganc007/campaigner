package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.dtos.climate.CreateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.UpdateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.ViewClimateDTO;
import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.modules.common.mappers.ClimateMapper;
import com.mcommings.campaigner.modules.common.repositories.IClimateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClimateService extends BaseService<
        Climate,
        Integer,
        ViewClimateDTO,
        CreateClimateDTO,
        UpdateClimateDTO> {

    private final IClimateRepository climateRepository;
    private final ClimateMapper climateMapper;

    @Override
    protected JpaRepository<Climate, Integer> getRepository() {
        return climateRepository;
    }

    @Override
    protected ViewClimateDTO toViewDto(Climate entity) {
        return climateMapper.toDto(entity);
    }

    @Override
    protected Climate toEntity(CreateClimateDTO dto) {

        Climate entity = climateMapper.toEntity(dto);

        return entity;
    }

    @Override
    protected void updateEntity(UpdateClimateDTO dto, Climate entity) {

        climateMapper.updateClimateFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateClimateDTO dto) {
        return dto.getId();
    }
}
