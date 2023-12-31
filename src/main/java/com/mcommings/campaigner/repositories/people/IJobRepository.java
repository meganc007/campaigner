package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.models.people.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJobRepository extends JpaRepository<Job, Integer> {
    Optional<Job> findByName(String name);

}
