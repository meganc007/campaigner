package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.items.dtos.weapons.CreateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.UpdateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.ViewWeaponDTO;
import com.mcommings.campaigner.modules.items.entities.Weapon;
import com.mcommings.campaigner.modules.items.mappers.WeaponMapper;
import com.mcommings.campaigner.modules.items.repositories.IWeaponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WeaponService extends BaseService<
        Weapon,
        Integer,
        ViewWeaponDTO,
        CreateWeaponDTO,
        UpdateWeaponDTO> {

    private final IWeaponRepository weaponRepository;
    private final ICampaignRepository campaignRepository;
    private final WeaponMapper weaponMapper;

    @Override
    protected JpaRepository<Weapon, Integer> getRepository() {
        return weaponRepository;
    }

    @Override
    protected ViewWeaponDTO toViewDto(Weapon entity) {
        return weaponMapper.toDto(entity);
    }

    @Override
    protected Weapon toEntity(CreateWeaponDTO dto) {
        Weapon entity = weaponMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateWeaponDTO dto, Weapon entity) {
        weaponMapper.updateWeaponFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateWeaponDTO dto) {
        return dto.getId();
    }

    public List<ViewWeaponDTO> getWeaponsByCampaignUUID(UUID uuid) {
        return query(weaponRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewWeaponDTO> getWeaponsByWeaponTypeId(int weaponTypeId) {
        return query(weaponRepository::findByWeaponType_Id, weaponTypeId);
    }

    public List<ViewWeaponDTO> getWeaponsByDamageTypeId(int damageTypeId) {
        return query(weaponRepository::findByDamageType_Id, damageTypeId);
    }

    public List<ViewWeaponDTO> getWeaponsByDiceTypeId(int diceTypeId) {
        return query(weaponRepository::findByDiceType_Id, diceTypeId);
    }
}
