package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.entities.locations.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITerrainRepository extends JpaRepository<Terrain, Integer> {
        Optional<Terrain> findByName(String name);
}
