package com.mcommings.campaigner.modules.locations.repositories;

import com.mcommings.campaigner.modules.locations.entities.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITerrainRepository extends JpaRepository<Terrain, Integer> {
        Optional<Terrain> findByName(String name);

        List<Terrain> findByIdIn(List<Integer> ids);
}
