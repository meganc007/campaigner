package com.mcommings.campaigner.locations.services.interfaces;

import com.mcommings.campaigner.locations.dtos.ContinentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IContinent {
    List<ContinentDTO> getContinents();

    Optional<ContinentDTO> getContinent(int continentId);

    List<ContinentDTO> getContinentsByCampaignUUID(UUID uuid);

    void saveContinent(ContinentDTO continent);

    void deleteContinent(int continentId);

    Optional<ContinentDTO> updateContinent(int continentId, ContinentDTO continent);

}
