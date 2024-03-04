package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.interfaces.items.IWeapon;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.Weapon;
import com.mcommings.campaigner.repositories.items.IDamageTypeRepository;
import com.mcommings.campaigner.repositories.items.IDiceTypeRepository;
import com.mcommings.campaigner.repositories.items.IWeaponRepository;
import com.mcommings.campaigner.repositories.items.IWeaponTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class WeaponService implements IWeapon {

    private final IWeaponRepository weaponRepository;
    private final IWeaponTypeRepository weaponTypeRepository;
    private final IDamageTypeRepository damageTypeRepository;
    private final IDiceTypeRepository diceTypeRepository;

    @Autowired
    public WeaponService(IWeaponRepository weaponRepository, IWeaponTypeRepository weaponTypeRepository,
                         IDamageTypeRepository damageTypeRepository, IDiceTypeRepository diceTypeRepository) {
        this.weaponRepository = weaponRepository;
        this.weaponTypeRepository = weaponTypeRepository;
        this.damageTypeRepository = damageTypeRepository;
        this.diceTypeRepository = diceTypeRepository;
    }

    @Override
    public List<Weapon> getWeapons() {
        return weaponRepository.findAll();
    }

    @Override
    public List<Weapon> getWeaponsByCampaignUUID(UUID uuid) {
        return weaponRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    @Transactional
    public void saveWeapon(Weapon weapon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(weapon)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(weaponRepository, weapon)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(weapon) &&
                RepositoryHelper.foreignKeyIsNotValid(weaponRepository, getListOfForeignKeyRepositories(), weapon)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        weaponRepository.saveAndFlush(weapon);
    }

    @Override
    @Transactional
    public void deleteWeapon(int weaponId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weaponRepository, weaponId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
// TODO: uncomment when class that uses Weapon as a fk is added
//        if (RepositoryHelper.isForeignKey(getReposWhereWeaponIsAForeignKey(), FK_WEAPON.columnName, weaponId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        weaponRepository.deleteById(weaponId);
    }

    @Override
    @Transactional
    public void updateWeapon(int weaponId, Weapon weapon) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(weaponRepository, weaponId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(weapon) &&
                RepositoryHelper.foreignKeyIsNotValid(weaponRepository, getListOfForeignKeyRepositories(), weapon)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Weapon weaponToUpdate = RepositoryHelper.getById(weaponRepository, weaponId);
        if (weapon.getName() != null) weaponToUpdate.setName(weapon.getName());
        if (weapon.getDescription() != null) weaponToUpdate.setDescription(weapon.getDescription());
        if (weapon.getRarity() != null) weaponToUpdate.setRarity(weapon.getRarity());
        if (weapon.getGold_value() >= 0) weaponToUpdate.setGold_value(weapon.getGold_value());
        if (weapon.getSilver_value() >= 0) weaponToUpdate.setSilver_value(weapon.getSilver_value());
        if (weapon.getCopper_value() >= 0) weaponToUpdate.setCopper_value(weapon.getCopper_value());
        if (weapon.getWeight() >= 0) weaponToUpdate.setWeight(weapon.getWeight());
        if (weapon.getFk_weapon_type() != null) weaponToUpdate.setFk_weapon_type(weapon.getFk_weapon_type());
        if (weapon.getFk_damage_type() != null) weaponToUpdate.setFk_damage_type(weapon.getFk_damage_type());
        if (weapon.getFk_dice_type() != null) weaponToUpdate.setFk_dice_type(weapon.getFk_dice_type());
        if (weapon.getNumber_of_dice() >= 1) weaponToUpdate.setNumber_of_dice(weapon.getNumber_of_dice());
        if (weapon.getDamage_modifier() >= 0) weaponToUpdate.setDamage_modifier(weapon.getDamage_modifier());
        if (weapon.getIsMagical() != null) weaponToUpdate.setIsMagical(weapon.getIsMagical());
        if (weapon.getIsCursed() != null) weaponToUpdate.setIsCursed(weapon.getIsCursed());
        if (weapon.getNotes() != null) weaponToUpdate.setNotes(weapon.getNotes());
    }

// TODO: uncomment when class that uses Weapon as a fk is added
//    private List<CrudRepository> getReposWhereWeaponIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }

    private boolean hasForeignKeys(Weapon weapon) {
        return weapon.getFk_weapon_type() != null ||
                weapon.getDamageType() != null ||
                weapon.getDiceType() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(weaponTypeRepository, damageTypeRepository, diceTypeRepository));
    }
}
