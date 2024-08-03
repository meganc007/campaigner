package com.mcommings.campaigner.interfaces.locations;

import com.mcommings.campaigner.models.locations.Location;

import java.util.UUID;

public interface ILocation {

    Location getLocation(UUID uuid);

}
