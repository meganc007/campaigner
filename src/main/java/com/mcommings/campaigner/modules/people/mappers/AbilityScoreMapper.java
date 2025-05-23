package com.mcommings.campaigner.modules.people.mappers;

import com.mcommings.campaigner.modules.people.dtos.AbilityScoreDTO;
import com.mcommings.campaigner.modules.people.entities.AbilityScore;
import org.mapstruct.Mapper;

@Mapper
public interface AbilityScoreMapper {
    AbilityScore mapFromAbilityScoreDto(AbilityScoreDTO dto);

    AbilityScoreDTO mapToAbilityScoreDto(AbilityScore abilityScore);
}
