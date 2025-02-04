package com.mcommings.campaigner.repositories;

import com.mcommings.campaigner.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICampaignRepository extends JpaRepository<Campaign, Integer> {

    Optional<Campaign> findByUuid(UUID uuid);

    Optional<Campaign> findByName(String name);

    void deleteByUuid(UUID uuid);

    Boolean existsByUuid(UUID uuid);
}
