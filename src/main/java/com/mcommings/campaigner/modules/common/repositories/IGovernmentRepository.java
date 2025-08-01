package com.mcommings.campaigner.modules.common.repositories;

import com.mcommings.campaigner.modules.common.entities.Government;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IGovernmentRepository extends JpaRepository<Government, Integer> {
    Optional<Government> findByName(String name);

}
