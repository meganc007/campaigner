package com.mcommings.campaigner.common.repositories;

import com.mcommings.campaigner.common.entities.Climate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClimateRepository extends JpaRepository<Climate, Integer> {

    Optional<Climate> findByName(String name);
}
