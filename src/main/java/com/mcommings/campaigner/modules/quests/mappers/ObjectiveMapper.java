package com.mcommings.campaigner.modules.quests.mappers;

import com.mcommings.campaigner.modules.quests.dtos.ObjectiveDTO;
import com.mcommings.campaigner.modules.quests.entities.Objective;
import org.mapstruct.Mapper;

@Mapper
public interface ObjectiveMapper {
    Objective mapFromObjectiveDto(ObjectiveDTO dto);

    ObjectiveDTO mapToObjectiveDto(Objective objective);
}
