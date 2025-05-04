package com.mcommings.campaigner.modules.common.repositories;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICampaignRepository extends JpaRepository<Campaign, UUID> {

    Optional<Campaign> findByUuid(UUID uuid);

    Optional<Campaign> findByName(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Campaign c WHERE c.uuid = :uuid")
    void deleteByUuid(UUID uuid);

    Boolean existsByUuid(UUID uuid);
}
