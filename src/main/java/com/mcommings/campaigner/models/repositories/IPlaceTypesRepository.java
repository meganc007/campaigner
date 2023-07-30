package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlaceTypesRepository extends JpaRepository<PlaceType, Integer> {
}
