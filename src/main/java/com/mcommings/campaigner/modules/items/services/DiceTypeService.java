package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.items.dtos.dice_types.CreateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.UpdateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.ViewDiceTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DiceType;
import com.mcommings.campaigner.modules.items.mappers.DiceTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IDiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiceTypeService extends BaseService<
        DiceType,
        Integer,
        ViewDiceTypeDTO,
        CreateDiceTypeDTO,
        UpdateDiceTypeDTO> {

    private final IDiceTypeRepository diceTypeRepository;
    private final DiceTypeMapper diceTypeMapper;

    @Override
    protected JpaRepository<DiceType, Integer> getRepository() {
        return diceTypeRepository;
    }

    @Override
    protected ViewDiceTypeDTO toViewDto(DiceType entity) {
        return diceTypeMapper.toDto(entity);
    }

    @Override
    protected DiceType toEntity(CreateDiceTypeDTO dto) {
        return diceTypeMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(UpdateDiceTypeDTO dto, DiceType entity) {
        diceTypeMapper.updateDiceTypeFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateDiceTypeDTO dto) {
        return dto.getId();
    }
}
