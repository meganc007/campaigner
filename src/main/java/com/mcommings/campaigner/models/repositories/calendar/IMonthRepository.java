package com.mcommings.campaigner.models.repositories.calendar;

import com.mcommings.campaigner.models.calendar.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMonthRepository extends JpaRepository<Month, Integer> {
        Optional<Month> findByName(String name);

        List<Month> findBySeason(String season);
}
