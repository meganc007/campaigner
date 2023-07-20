package com.mcommings.campaigner.locations;

import com.mcommings.campaigner.governments.Government;
import com.mcommings.campaigner.locations.sublocations.City;
import com.mcommings.campaigner.locations.sublocations.Continent;
import com.mcommings.campaigner.locations.sublocations.Country;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class LocationService {

    Government government1 = new Government(1, "democracy", "people vote for stuff");
    Government government2 = new Government(2, "oligarcy", "rich old people rule");
    Government government3 = new Government(3, "fiefdom", "peasants belong to a knight");
    City city1 = new City(1, "city 1", "city", new Random().nextLong(), government3, "sticks and stones");
    City city2 = new City(2, "city 2", "village", new Random().nextLong(), government2, "cannons");
    City city3 = new City(3, "city 3", "town", new Random().nextLong(), government2, "a crazy old enchanter named Tim");
    City city4 = new City(4, "city 4", "hamlet", new Random().nextLong(), government2, "town militia");

    List<City> cities1 = List.of(city1, city2);
    List<City> cities2 = List.of(city3, city4);

    Country country1 = new Country(1, "country 1", government1, "this is a description 1", "mountainous", "desert", cities1);
    Country country2 = new Country(2, "country 2", government2, "this is a description 1", "marshy", "rainy", cities2);
    List<Country> countries = List.of(country1, country2);
    Continent continent = new Continent(1, "continent 1", countries);
    Location location = new Location(1, List.of(continent));

    public List<Location> getLocations() {
        return List.of(location);
    }
}
