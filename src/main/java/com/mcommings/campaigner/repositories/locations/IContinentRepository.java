package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.Continent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IContinentRepository extends JpaRepository<Continent, Integer> {
    Optional<Continent> findByName(String name);
}
