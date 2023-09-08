package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.interfaces.quests.IReward;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.quests.Reward;
import com.mcommings.campaigner.repositories.items.IItemRepository;
import com.mcommings.campaigner.repositories.items.IWeaponRepository;
import com.mcommings.campaigner.repositories.quests.IRewardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
public class RewardService implements IReward {

    private final IRewardRepository rewardRepository;
    private final IItemRepository itemRepository;
    private final IWeaponRepository weaponRepository;

    @Autowired
    public RewardService(IRewardRepository rewardRepository, IItemRepository itemRepository,
                         IWeaponRepository weaponRepository) {
        this.rewardRepository = rewardRepository;
        this.itemRepository = itemRepository;
        this.weaponRepository = weaponRepository;
    }

    @Override
    public List<Reward> getRewards() {
        return rewardRepository.findAll();
    }

    @Override
    @Transactional
    public void saveReward(Reward reward) throws IllegalArgumentException, DataIntegrityViolationException {
        if (descriptionIsNullOrEmpty(reward.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (rewardAlreadyExists(reward)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(reward) &&
                RepositoryHelper.foreignKeyIsNotValid(rewardRepository, getListOfForeignKeyRepositories(), reward)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        rewardRepository.saveAndFlush(reward);
    }

    @Override
    @Transactional
    public void deleteReward(int rewardId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(rewardRepository, rewardId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
//        TODO: uncomment when Reward is used as a fk
//        if (RepositoryHelper.isForeignKey(getReposWhereRewardIsAForeignKey(), FK_REWARD.columnName, rewardId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        rewardRepository.deleteById(rewardId);
    }

    @Override
    @Transactional
    public void updateReward(int rewardId, Reward reward) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(rewardRepository, rewardId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(reward) &&
                RepositoryHelper.foreignKeyIsNotValid(rewardRepository, getListOfForeignKeyRepositories(), reward)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Reward rewardToUpdate = RepositoryHelper.getById(rewardRepository, rewardId);
        rewardToUpdate.setDescription(reward.getDescription());
        rewardToUpdate.setNotes(reward.getNotes());
        rewardToUpdate.setGold_value(reward.getGold_value());
        rewardToUpdate.setSilver_value(reward.getSilver_value());
        rewardToUpdate.setCopper_value(reward.getCopper_value());
        rewardToUpdate.setFk_item(reward.getFk_item());
        rewardToUpdate.setFk_weapon(reward.getFk_weapon());
    }

    private boolean descriptionIsNullOrEmpty(String description) {
        return isNull(description) || description.isEmpty();
    }

    private boolean rewardAlreadyExists(Reward reward) {
        return rewardRepository.rewardExists(reward).isPresent();
    }

//    TODO: fix once Reward is used as fk
//    private List<CrudRepository> getReposWhereRewardIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }

    private boolean hasForeignKeys(Reward reward) {
        return reward.getFk_item() != null ||
                reward.getFk_weapon() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(itemRepository, weaponRepository));
    }
}
