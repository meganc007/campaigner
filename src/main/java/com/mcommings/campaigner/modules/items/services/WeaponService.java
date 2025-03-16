package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.items.dtos.WeaponDTO;
import com.mcommings.campaigner.modules.items.mappers.WeaponMapper;
import com.mcommings.campaigner.modules.items.repositories.IWeaponRepository;
import com.mcommings.campaigner.modules.items.services.interfaces.IWeapon;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class WeaponService implements IWeapon {

    private final IWeaponRepository weaponRepository;
    private final WeaponMapper weaponMapper;

    @Override
    public List<WeaponDTO> getWeapons() {
        return weaponRepository.findAll()
                .stream()
                .map(weaponMapper::mapToWeaponDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WeaponDTO> getWeapon(int weaponId) {
        return weaponRepository.findById(weaponId)
                .map(weaponMapper::mapToWeaponDto);
    }

    @Override
    public List<WeaponDTO> getWeaponsByCampaignUUID(UUID uuid) {

        return weaponRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(weaponMapper::mapToWeaponDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveWeapon(WeaponDTO weapon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(weapon)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(weaponRepository, weapon.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        weaponMapper.mapToWeaponDto(
                weaponRepository.save(weaponMapper.mapFromWeaponDto(weapon)
                ));
    }

    @Override
    @Transactional
    public void deleteWeapon(int weaponId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weaponRepository, weaponId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        weaponRepository.deleteById(weaponId);
    }

    @Override
    @Transactional
    public Optional<WeaponDTO> updateWeapon(int weaponId, WeaponDTO weapon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weaponRepository, weaponId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(weapon)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(weaponRepository, weapon.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return weaponRepository.findById(weaponId).map(foundWeapon -> {
            if (weapon.getName() != null) foundWeapon.setName(weapon.getName());
            if (weapon.getDescription() != null) foundWeapon.setDescription(weapon.getDescription());
            if (weapon.getFk_campaign_uuid() != null) foundWeapon.setFk_campaign_uuid(weapon.getFk_campaign_uuid());
            if (weapon.getRarity() != null) foundWeapon.setRarity(weapon.getRarity());
            if (weapon.getGold_value() >= 0) foundWeapon.setGold_value(weapon.getGold_value());
            if (weapon.getSilver_value() >= 0) foundWeapon.setSilver_value(weapon.getSilver_value());
            if (weapon.getCopper_value() >= 0) foundWeapon.setCopper_value(weapon.getCopper_value());
            if (weapon.getWeight() >= 0) foundWeapon.setWeight(weapon.getWeight());
            if (weapon.getFk_weapon_type() != null) foundWeapon.setFk_weapon_type(weapon.getFk_weapon_type());
            if (weapon.getFk_damage_type() != null) foundWeapon.setFk_damage_type(weapon.getFk_damage_type());
            if (weapon.getFk_dice_type() != null) foundWeapon.setFk_dice_type(weapon.getFk_dice_type());
            if (weapon.getNumber_of_dice() >= 1) foundWeapon.setNumber_of_dice(weapon.getNumber_of_dice());
            if (weapon.getDamage_modifier() >= 0) foundWeapon.setDamage_modifier(weapon.getDamage_modifier());
            if (weapon.getIsMagical() != null) foundWeapon.setIsMagical(weapon.getIsMagical());
            if (weapon.getIsCursed() != null) foundWeapon.setIsCursed(weapon.getIsCursed());
            if (weapon.getNotes() != null) foundWeapon.setNotes(weapon.getNotes());

            return weaponMapper.mapToWeaponDto(weaponRepository.save(foundWeapon));
        });
    }
}
