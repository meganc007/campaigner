package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Region;

import java.util.List;
import java.util.UUID;

public interface IRegion {

    List<Region> getRegions();

    Region getRegion(int regionId);

    List<Region> getRegionsByCampaignUUID(UUID uuid);

    List<Region> getRegionsByCountryId(int countryId);

    void saveRegion(Region region);

    void deleteRegion(int regionId);

    void updateRegion(int regionId, Region region);
}
