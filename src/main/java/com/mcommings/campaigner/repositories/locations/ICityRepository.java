package com.mcommings.campaigner.repositories.locations;

import com.mcommings.campaigner.models.locations.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICityRepository extends JpaRepository<City, Integer> {

    Optional<City> findByName(String name);

    @Query("SELECT c FROM City c WHERE c.fk_wealth = :id")
    List<City> findByfk_wealth(@Param("id") Integer id);

    @Query("SELECT c FROM City c WHERE c.fk_country = :id")
    List<City> findByfk_country(@Param("id") Integer id);

    @Query("SELECT c FROM City c WHERE c.fk_settlement = :id")
    List<City> findByfk_settlement(@Param("id") Integer id);

    @Query("SELECT c FROM City c WHERE c.fk_government = :id")
    List<City> findByfk_government(@Param("id") Integer id);

    @Query("SELECT c FROM City c WHERE c.fk_region = :id")
    List<City> findByfk_region(@Param("id") Integer id);
}
