package com.mcommings.campaigner.modules.calendar.repositories;

import com.mcommings.campaigner.modules.calendar.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IEventRepository extends JpaRepository<Event, Integer> {

    Optional<Event> findByName(String name);

    List<Event> findByCampaign_Uuid(UUID uuid);

    List<Event> findByMonth_Id(Integer monthId);

    List<Event> findByWeek_Id(Integer weekId);

    List<Event> findByDay_Id(Integer dayId);

    List<Event> findByYear_Id(int yearId);

    List<Event> findByContinent_Id(Integer continentId);

    List<Event> findByCountry_Id(Integer countryId);

    List<Event> findByCity_Id(Integer cityId);

    boolean existsByNameAndCampaign_UuidAndIdNot(String name, UUID campaignUuid, Integer id);

    boolean existsByNameAndCampaign_Uuid(String name, UUID campaignUuid);
}
