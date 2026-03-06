package com.mcommings.campaigner.modules.locations.services.interfaces;

import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IContinent {
    List<ViewContinentDTO> getContinents();

    Optional<ViewContinentDTO> getContinent(int continentId);

    List<ViewContinentDTO> getContinentsByCampaignUUID(UUID uuid);

    ViewContinentDTO saveContinent(CreateContinentDTO continent);

    Optional<ViewContinentDTO> updateContinent(UpdateContinentDTO continent);

    void deleteContinent(int continentId);

}
