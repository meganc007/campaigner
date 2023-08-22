package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByName(String name);
}
