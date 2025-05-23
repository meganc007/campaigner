package com.mcommings.campaigner.modules.quests.mappers;

import com.mcommings.campaigner.modules.quests.dtos.HookDTO;
import com.mcommings.campaigner.modules.quests.entities.Hook;
import org.mapstruct.Mapper;

@Mapper
public interface HookMapper {
    Hook mapFromHookDto(HookDTO dto);

    HookDTO mapToHookDto(Hook hook);
}
