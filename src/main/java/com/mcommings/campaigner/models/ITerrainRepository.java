package com.mcommings.campaigner.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITerrainRepository extends JpaRepository<Terrain, Integer> {
}
