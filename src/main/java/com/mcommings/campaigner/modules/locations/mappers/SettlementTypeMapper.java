package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.CreateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.UpdateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.ViewSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface SettlementTypeMapper {

    @Mapping(target = "id", ignore = true)
    SettlementType toEntity(CreateSettlementTypeDTO dto);

    ViewSettlementTypeDTO toDto(SettlementType settlementType);

    void updateSettlementTypeFromDto(
            UpdateSettlementTypeDTO dto,
            @MappingTarget SettlementType entity
    );
}
