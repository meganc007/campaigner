package com.mcommings.campaigner.modules.locations.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.CreateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.UpdateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.ViewSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import com.mcommings.campaigner.modules.locations.mappers.SettlementTypeMapper;
import com.mcommings.campaigner.modules.locations.repositories.ISettlementTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettlementTypeService extends BaseService<
        SettlementType,
        Integer,
        ViewSettlementTypeDTO,
        CreateSettlementTypeDTO,
        UpdateSettlementTypeDTO> {

    private final ISettlementTypeRepository settlementTypeRepository;
    private final SettlementTypeMapper settlementTypeMapper;

    @Override
    protected JpaRepository<SettlementType, Integer> getRepository() {
        return settlementTypeRepository;
    }

    @Override
    protected ViewSettlementTypeDTO toViewDto(SettlementType entity) {
        return settlementTypeMapper.toDto(entity);
    }

    @Override
    protected SettlementType toEntity(CreateSettlementTypeDTO dto) {
        return settlementTypeMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(UpdateSettlementTypeDTO dto, SettlementType entity) {
        settlementTypeMapper.updateSettlementTypeFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateSettlementTypeDTO dto) {
        return dto.getId();
    }
}
