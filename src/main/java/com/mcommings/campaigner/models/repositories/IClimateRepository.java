package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Climate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClimateRepository extends JpaRepository<Climate, Integer> {
}
