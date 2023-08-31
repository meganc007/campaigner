package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILandmarkRepository extends JpaRepository<Landmark, Integer> {
    Optional<Landmark> findByName(String name);

}
