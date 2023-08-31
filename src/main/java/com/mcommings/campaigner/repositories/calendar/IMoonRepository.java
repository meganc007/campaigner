package com.mcommings.campaigner.repositories.calendar;

import com.mcommings.campaigner.models.calendar.Moon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMoonRepository extends JpaRepository<Moon, Integer> {

    Optional<Moon> getByName(String name);
}
