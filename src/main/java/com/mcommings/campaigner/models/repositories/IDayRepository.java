package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDayRepository extends JpaRepository<Day, Integer> {

    Optional<Day> getByName(String name);

    @Query("SELECT d FROM Day d WHERE d.fk_week = :id")
    List<Day> getByfk_week(@Param("id") Integer id);
}
