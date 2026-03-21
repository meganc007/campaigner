package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.dtos.government.CreateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.UpdateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.ViewGovernmentDTO;
import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.modules.common.mappers.GovernmentMapper;
import com.mcommings.campaigner.modules.common.repositories.IGovernmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GovernmentService extends BaseService<
        Government,
        Integer,
        ViewGovernmentDTO,
        CreateGovernmentDTO,
        UpdateGovernmentDTO> {

    private final IGovernmentRepository governmentRepository;
    private final GovernmentMapper governmentMapper;

    @Override
    protected JpaRepository<Government, Integer> getRepository() {
        return governmentRepository;
    }

    @Override
    protected ViewGovernmentDTO toViewDto(Government entity) {
        return governmentMapper.toDto(entity);
    }

    @Override
    protected Government toEntity(CreateGovernmentDTO dto) {

        Government entity = governmentMapper.toEntity(dto);

        return entity;
    }

    @Override
    protected void updateEntity(UpdateGovernmentDTO dto, Government entity) {

        governmentMapper.updateGovernmentFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateGovernmentDTO dto) {
        return dto.getId();
    }
}
