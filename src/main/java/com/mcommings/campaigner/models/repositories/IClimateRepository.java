package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Climate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClimateRepository extends JpaRepository<Climate, Integer> {

    Optional<Climate> findByName(String name);
}
