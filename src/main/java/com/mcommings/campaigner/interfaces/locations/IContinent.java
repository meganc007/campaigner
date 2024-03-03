package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Continent;

import java.util.List;

public interface IContinent {
    List<Continent> getContinents();

    Continent getContinent(int continentId);

    void saveContinent(Continent continent);

    void deleteContinent(int continentId);

    void updateContinent(int continentId, Continent continent);
    
}
