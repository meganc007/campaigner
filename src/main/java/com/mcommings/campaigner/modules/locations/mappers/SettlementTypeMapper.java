package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.SettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import org.mapstruct.Mapper;

@Mapper
public interface SettlementTypeMapper {

    SettlementType mapFromSettlementTypeDto(SettlementTypeDTO dto);

    SettlementTypeDTO mapToSettlementTypeDto(SettlementType settlementType);
}
