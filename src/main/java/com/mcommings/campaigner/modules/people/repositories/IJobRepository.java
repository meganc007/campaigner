package com.mcommings.campaigner.modules.people.repositories;

import com.mcommings.campaigner.modules.people.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IJobRepository extends JpaRepository<Job, Integer> {

    Optional<Job> findByName(String name);

    List<Job> findAllByOrderByNameAsc();

}
