package com.mcommings.campaigner.modules.quests.mappers;

import com.mcommings.campaigner.modules.quests.dtos.RewardDTO;
import com.mcommings.campaigner.modules.quests.entities.Reward;
import org.mapstruct.Mapper;

@Mapper
public interface RewardMapper {
    Reward mapFromRewardDto(RewardDTO dto);

    RewardDTO mapToRewardDto(Reward reward);
}
