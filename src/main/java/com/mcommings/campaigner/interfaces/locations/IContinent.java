package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Continent;

import java.util.List;
import java.util.UUID;

public interface IContinent {
    List<Continent> getContinents();

    Continent getContinent(int continentId);

    List<Continent> getContinentsByCampaignUUID(UUID uuid);

    void saveContinent(Continent continent);

    void deleteContinent(int continentId);

    void updateContinent(int continentId, Continent continent);

}
