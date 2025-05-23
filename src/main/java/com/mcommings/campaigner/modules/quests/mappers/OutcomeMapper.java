package com.mcommings.campaigner.modules.quests.mappers;

import com.mcommings.campaigner.modules.quests.dtos.OutcomeDTO;
import com.mcommings.campaigner.modules.quests.entities.Outcome;
import org.mapstruct.Mapper;

@Mapper
public interface OutcomeMapper {
    Outcome mapFromOutcomeDto(OutcomeDTO dto);

    OutcomeDTO mapToOutcomeDto(Outcome outcome);
}
