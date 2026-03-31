package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.items.dtos.damage_types.CreateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.UpdateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.ViewDamageTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.modules.items.mappers.DamageTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IDamageTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DamageTypeService extends BaseService<
        DamageType,
        Integer,
        ViewDamageTypeDTO,
        CreateDamageTypeDTO,
        UpdateDamageTypeDTO> {

    private final IDamageTypeRepository damageTypeRepository;
    private final DamageTypeMapper damageTypeMapper;

    @Override
    protected JpaRepository<DamageType, Integer> getRepository() {
        return damageTypeRepository;
    }

    @Override
    protected ViewDamageTypeDTO toViewDto(DamageType entity) {
        return damageTypeMapper.toDto(entity);
    }

    @Override
    protected DamageType toEntity(CreateDamageTypeDTO dto) {
        return damageTypeMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(UpdateDamageTypeDTO dto, DamageType entity) {
        damageTypeMapper.updateDamageTypeFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateDamageTypeDTO dto) {
        return dto.getId();
    }
}
