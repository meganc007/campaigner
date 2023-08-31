package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Region;

import java.util.List;

public interface IRegion {
    
    List<Region> getRegions();

    void saveRegion(Region region);

    void deleteRegion(int regionId);

    void updateRegion(int regionId, Region region);
}
