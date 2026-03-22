package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IDayRepository extends JpaRepository<Day, Integer> {

    Optional<Day> findByName(String name);

    List<Day> findByCampaign_Uuid(UUID uuid);

    List<Day> findByWeek_Id(Integer id);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
