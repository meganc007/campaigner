package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJobRepository extends JpaRepository<Job, Integer> {
    Optional<Job> findByName(String name);

}
