package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.models.people.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRaceRepository extends JpaRepository<Race, Integer> {
    Optional<Race> findByName(String name);
}
