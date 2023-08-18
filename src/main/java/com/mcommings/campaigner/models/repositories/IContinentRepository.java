package com.mcommings.campaigner.models.repositories;

import com.mcommings.campaigner.models.Continent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContinentRepository extends JpaRepository<Continent, Integer> {
}
