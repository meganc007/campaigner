package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILandmarkRepository extends JpaRepository<Landmark, Integer> {
    Optional<Landmark> findByName(String name);

}
