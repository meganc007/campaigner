package com.mcommings.campaigner.modules.common.repositories;

import com.mcommings.campaigner.modules.common.entities.Climate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClimateRepository extends JpaRepository<Climate, Integer> {

    Optional<Climate> findByName(String name);

    List<Climate> findByIdIn(List<Integer> ids);
}
