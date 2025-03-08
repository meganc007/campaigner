package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.dtos.ContinentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IContinent {
    List<ContinentDTO> getContinents();

    Optional<ContinentDTO> getContinent(int continentId);

    List<ContinentDTO> getContinentsByCampaignUUID(UUID uuid);

    void saveContinent(ContinentDTO continent);

    Boolean deleteContinent(int continentId);

    Optional<ContinentDTO> updateContinent(int continentId, ContinentDTO continent);

}
