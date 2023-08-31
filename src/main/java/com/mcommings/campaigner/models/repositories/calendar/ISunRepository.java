package com.mcommings.campaigner.models.repositories.calendar;

import com.mcommings.campaigner.models.calendar.Sun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISunRepository extends JpaRepository<Sun, Integer> {
    Optional<Sun> getByName(String name);
}
