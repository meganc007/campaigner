package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IWeekRepository extends JpaRepository<Week, Integer> {

    Optional<Week> findByName(String name);

    List<Week> findByCampaign_Uuid(UUID uuid);

    List<Week> findByMonth_Id(Integer monthId);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
