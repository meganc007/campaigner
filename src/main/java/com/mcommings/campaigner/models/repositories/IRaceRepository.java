package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRaceRepository extends JpaRepository<Race, Integer> {
    Optional<Race> findRaceByName(String name);
}
