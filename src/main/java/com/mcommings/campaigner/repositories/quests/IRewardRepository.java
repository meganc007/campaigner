package com.mcommings.campaigner.repositories.quests;

import com.mcommings.campaigner.models.quests.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRewardRepository extends JpaRepository<Reward, Integer> {

    Optional<Reward> findByDescription(String description);

    @Query("SELECT r FROM Reward r WHERE r.description = :#{#reward.description}")
    Optional<Reward> rewardExists(@Param("reward") Reward reward);
}
