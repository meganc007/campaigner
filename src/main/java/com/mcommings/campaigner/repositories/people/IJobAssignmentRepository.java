package com.mcommings.campaigner.repositories.people;

import com.mcommings.campaigner.models.people.JobAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IJobAssignmentRepository extends JpaRepository<JobAssignment, Integer> {

    @Query("SELECT j FROM JobAssignment j WHERE " +
            "j.fk_person = :#{#jobAssignment.fk_person} AND " +
            "j.fk_job = :#{#jobAssignment.fk_job}")
    Optional<JobAssignment> jobAssignmentExists(@Param("jobAssignment") JobAssignment jobAssignment);

    @Query("SELECT j FROM JobAssignment j WHERE j.fk_person = :id")
    List<JobAssignment> findByfk_person(@Param("id") Integer id);

    @Query("SELECT j FROM JobAssignment j WHERE j.fk_job = :id")
    List<JobAssignment> findByfk_job(@Param("id") Integer id);
}
