package com.mcommings.campaigner.modules.quests.repositories;

import com.mcommings.campaigner.modules.quests.entities.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRewardRepository extends JpaRepository<Reward, Integer> {

    @Query("SELECT r FROM Reward r WHERE LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Reward> searchByDescription(@Param("keyword") String keyword);

    @Query("SELECT r FROM Reward r WHERE r.description = :#{#reward.description}")
    Optional<Reward> rewardExists(@Param("reward") Reward reward);

    @Query("SELECT r FROM Reward r WHERE r.fk_campaign_uuid = :uuid")
    List<Reward> findByfk_campaign_uuid(@Param("uuid") UUID uuid);

    @Query("SELECT r FROM Reward r WHERE r.fk_item = :id")
    List<Reward> findByfk_item(@Param("id") Integer id);

    @Query("SELECT r FROM Reward r WHERE r.fk_weapon = :id")
    List<Reward> findByfk_weapon(@Param("id") Integer id);
}
