package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPlaceTypesRepository extends JpaRepository<PlaceType, Integer> {
    Optional<PlaceType> findByName(String name);

    List<PlaceType> findByIdIn(List<Integer> ids);
}
