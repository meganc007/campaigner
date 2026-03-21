package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICelestialEventRepository extends JpaRepository<CelestialEvent, Integer> {

    Optional<CelestialEvent> findByName(String name);

    List<CelestialEvent> findByCampaign_Uuid(UUID uuid);

    List<CelestialEvent> findByMoon_Id(Integer moonId);

    List<CelestialEvent> findBySun_Id(Integer sunId);

    List<CelestialEvent> findByMonth_Id(Integer monthId);

    List<CelestialEvent> findByWeek_Id(Integer weekId);

    List<CelestialEvent> findByDay_Id(Integer dayId);

    List<CelestialEvent> findByYear_Id(int yearId);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
