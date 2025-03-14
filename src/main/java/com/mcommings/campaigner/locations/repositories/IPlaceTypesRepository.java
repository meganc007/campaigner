package com.mcommings.campaigner.locations.repositories;

import com.mcommings.campaigner.locations.entities.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlaceTypesRepository extends JpaRepository<PlaceType, Integer> {
    Optional<PlaceType> findByName(String name);
}
