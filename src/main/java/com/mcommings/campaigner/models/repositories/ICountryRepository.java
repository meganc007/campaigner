package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICountryRepository extends JpaRepository<Country, Integer> {
}
