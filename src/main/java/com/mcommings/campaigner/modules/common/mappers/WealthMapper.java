package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.modules.common.dtos.WealthDTO;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import org.mapstruct.Mapper;

@Mapper
public interface WealthMapper {
    Wealth mapFromWealthDto(WealthDTO dto);

    WealthDTO mapToWealthDto(Wealth wealth);
}
