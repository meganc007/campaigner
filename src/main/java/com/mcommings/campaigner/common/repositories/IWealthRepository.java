package com.mcommings.campaigner.common.repositories;

import com.mcommings.campaigner.common.entities.Wealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IWealthRepository extends JpaRepository<Wealth, Integer> {
        Optional<Wealth> findByName(String name);
}
