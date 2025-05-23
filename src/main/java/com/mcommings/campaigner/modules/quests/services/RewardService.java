package com.mcommings.campaigner.modules.quests.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.quests.dtos.RewardDTO;
import com.mcommings.campaigner.modules.quests.entities.Reward;
import com.mcommings.campaigner.modules.quests.mappers.RewardMapper;
import com.mcommings.campaigner.modules.quests.repositories.IRewardRepository;
import com.mcommings.campaigner.modules.quests.services.interfaces.IReward;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class RewardService implements IReward {

    private final IRewardRepository rewardRepository;
    private final RewardMapper rewardMapper;

    @Override
    public List<RewardDTO> getRewards() {
        return rewardRepository.findAll()
                .stream()
                .map(rewardMapper::mapToRewardDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RewardDTO> getReward(int rewardId) {
        return rewardRepository.findById(rewardId)
                .map(rewardMapper::mapToRewardDto);
    }

    @Override
    public List<RewardDTO> getRewardsByCampaignUUID(UUID uuid) {

        return rewardRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(rewardMapper::mapToRewardDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RewardDTO> getRewardsWhereDescriptionContainsKeyword(String keyword) throws IllegalArgumentException {
        if (isNullOrEmpty(keyword)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return rewardRepository.searchByDescription(keyword)
                .stream()
                .map(rewardMapper::mapToRewardDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RewardDTO> getRewardsByItemId(int itemId) {
        return rewardRepository.findByfk_item(itemId)
                .stream()
                .map(rewardMapper::mapToRewardDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RewardDTO> getRewardsByWeaponId(int weaponId) {
        return rewardRepository.findByfk_weapon(weaponId)
                .stream()
                .map(rewardMapper::mapToRewardDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveReward(RewardDTO reward) throws IllegalArgumentException, DataIntegrityViolationException {
        if (isNullOrEmpty(reward.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (rewardAlreadyExists(rewardMapper.mapFromRewardDto(reward))) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        rewardMapper.mapToRewardDto(
                rewardRepository.save(rewardMapper.mapFromRewardDto(reward)
                ));
    }

    @Override
    public void deleteReward(int rewardId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(rewardRepository, rewardId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        rewardRepository.deleteById(rewardId);
    }

    @Override
    public Optional<RewardDTO> updateReward(int rewardId, RewardDTO reward) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(rewardRepository, rewardId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (isNullOrEmpty(reward.getDescription())) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }

        return rewardRepository.findById(rewardId).map(foundReward -> {
            if (reward.getDescription() != null) foundReward.setDescription(reward.getDescription());
            if (reward.getNotes() != null) foundReward.setNotes(reward.getNotes());
            if (reward.getGold_value() >= 0) foundReward.setGold_value(reward.getGold_value());
            if (reward.getSilver_value() >= 0) foundReward.setSilver_value(reward.getSilver_value());
            if (reward.getCopper_value() >= 0) foundReward.setCopper_value(reward.getCopper_value());
            if (reward.getFk_item() != null) foundReward.setFk_item(reward.getFk_item());
            if (reward.getFk_weapon() != null) foundReward.setFk_weapon(reward.getFk_weapon());

            return rewardMapper.mapToRewardDto(rewardRepository.save(foundReward));
        });
    }

    private boolean isNullOrEmpty(String input) {
        return isNull(input) || input.isEmpty();
    }

    private boolean rewardAlreadyExists(Reward reward) {
        return rewardRepository.rewardExists(reward).isPresent();
    }

}
