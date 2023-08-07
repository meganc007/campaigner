package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Government;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGovernmentRepository extends JpaRepository<Government, Integer> {
}
