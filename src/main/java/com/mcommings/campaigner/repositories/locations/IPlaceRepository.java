package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPlaceRepository extends JpaRepository<Place, Integer> {

    Optional<Place> findByName(String name);

    @Query("SELECT p FROM Place p WHERE p.fk_place_type = :id")
    List<Place> findByfk_place_type(@Param("id") Integer id);

    @Query("SELECT p FROM Place p WHERE p.fk_terrain = :id")
    List<Place> findByfk_terrain(@Param("id") Integer id);

    @Query("SELECT p FROM Place p WHERE p.fk_country = :id")
    List<Place> findByfk_country(@Param("id") Integer id);

    @Query("SELECT p FROM Place p WHERE p.fk_city = :id")
    List<Place> findByfk_city(@Param("id") Integer id);

    @Query("SELECT p FROM Place p WHERE p.fk_region = :id")
    List<Place> findByfk_region(@Param("id") Integer id);
}
