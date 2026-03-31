package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.CreateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.UpdateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.ViewWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.entities.WeaponType;
import com.mcommings.campaigner.modules.items.mappers.WeaponTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IWeaponTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeaponTypeService extends BaseService<
        WeaponType,
        Integer,
        ViewWeaponTypeDTO,
        CreateWeaponTypeDTO,
        UpdateWeaponTypeDTO> {

    private final IWeaponTypeRepository weaponTypeRepository;
    private final WeaponTypeMapper weaponTypeMapper;

    @Override
    protected JpaRepository<WeaponType, Integer> getRepository() {
        return weaponTypeRepository;
    }

    @Override
    protected ViewWeaponTypeDTO toViewDto(WeaponType entity) {
        return weaponTypeMapper.toDto(entity);
    }

    @Override
    protected WeaponType toEntity(CreateWeaponTypeDTO dto) {
        return weaponTypeMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(UpdateWeaponTypeDTO dto, WeaponType entity) {
        weaponTypeMapper.updateWeaponTypeFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateWeaponTypeDTO dto) {
        return dto.getId();
    }
}
