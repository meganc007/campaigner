package com.mcommings.campaigner.modules.locations.services.interfaces;

import com.mcommings.campaigner.modules.locations.dtos.RegionDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRegion {

    List<RegionDTO> getRegions();

    Optional<RegionDTO> getRegion(int regionId);

    List<RegionDTO> getRegionsByCampaignUUID(UUID uuid);

    List<RegionDTO> getRegionsByCountryId(int countryId);

    void saveRegion(RegionDTO region);

    void deleteRegion(int regionId);

    Optional<RegionDTO> updateRegion(int regionId, RegionDTO region);
}
