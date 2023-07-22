package com.mcommings.campaigner.services;

import com.mcommings.campaigner.governments.Government;
import com.mcommings.campaigner.locations.Location;
import com.mcommings.campaigner.locations.sublocations.Continent;
import com.mcommings.campaigner.locations.sublocations.Country;
import com.mcommings.campaigner.interfaces.ILocation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService implements ILocation {

    Government government1 = new Government(1, "democracy", "people vote for stuff");
    Government government2 = new Government(2, "oligarcy", "rich old people rule");
    Government government3 = new Government(3, "fiefdom", "peasants belong to a knight");
    Country country1 = new Country(1, "country 1", government1, "this is a description 1");
    Country country2 = new Country(2, "country 2", government2, "this is a description 1");
    List<Country> countries = List.of(country1, country2);
    Continent continent = new Continent(1, "continent 1", countries);
    Location location = new Location(1, List.of(continent));

    @Override
    public List<Location> getLocations() {
        return List.of(location);
    }
}
