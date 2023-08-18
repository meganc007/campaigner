package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Continent;

import java.util.List;

public interface IContinent {
    List<Continent> getContinents();

    void saveContinent(Continent continent);

    void deleteContinent(int continentId);

    void updateContinent(int continentId, Continent continent);
    
}
