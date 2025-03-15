package com.mcommings.campaigner.modules.people.repositories;

import com.mcommings.campaigner.modules.people.entities.JobAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Query("SELECT j FROM JobAssignment j WHERE j.fk_campaign_uuid = :uuid")
    List<JobAssignment> findByfk_campaign_uuid(@Param("uuid") UUID uuid);
}
