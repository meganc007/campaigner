package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICityRepository extends JpaRepository<City, Integer> {

    Optional<City> findByName(String name);
}
