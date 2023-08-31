package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlaceTypesRepository extends JpaRepository<PlaceType, Integer> {
    Optional<PlaceType> findByName(String name);
}
