package com.mcommings.campaigner.repositories;

import com.mcommings.campaigner.entities.Wealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IWealthRepository extends JpaRepository<Wealth, Integer> {
        Optional<Wealth> findByName(String name);
}
