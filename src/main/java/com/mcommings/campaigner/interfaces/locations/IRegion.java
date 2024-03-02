package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Region;

import java.util.List;

public interface IRegion {

    List<Region> getRegions();

    Region getRegion(int regionId);

    void saveRegion(Region region);

    void deleteRegion(int regionId);

    void updateRegion(int regionId, Region region);
}
