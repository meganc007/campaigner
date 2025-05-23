package com.mcommings.campaigner.modules.people.repositories;

import com.mcommings.campaigner.modules.people.entities.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRaceRepository extends JpaRepository<Race, Integer> {
    Optional<Race> findByName(String name);

    List<Race> findByIsExotic(Boolean isExotic);
}
