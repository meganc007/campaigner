package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.SettlementTypeDTO;
import com.mcommings.campaigner.locations.entities.SettlementType;
import org.mapstruct.Mapper;

@Mapper
public interface SettlementTypeMapper {

    SettlementType mapFromSettlementType(SettlementTypeDTO dto);

    SettlementTypeDTO mapToSettlementTypeDto(SettlementType settlementType);
}
