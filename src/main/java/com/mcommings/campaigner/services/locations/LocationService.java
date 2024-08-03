package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.interfaces.locations.ILocation;
import com.mcommings.campaigner.models.locations.Location;
import com.mcommings.campaigner.repositories.locations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocationService implements ILocation {

    private final IContinentRepository continentRepository;
    private final ICountryRepository countryRepository;
    private final IRegionRepository regionRepository;
    private final ICityRepository cityRepository;
    private final IPlaceRepository placeRepository;
    private final ILandmarkRepository landmarkRepository;

    @Autowired
    public LocationService(IContinentRepository continentRepository, ICountryRepository countryRepository,
                           IRegionRepository regionRepository, ICityRepository cityRepository,
                           IPlaceRepository placeRepository, ILandmarkRepository landmarkRepository) {
        this.continentRepository = continentRepository;
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.cityRepository = cityRepository;
        this.placeRepository = placeRepository;
        this.landmarkRepository = landmarkRepository;
    }

    @Override
    public Location getLocation(UUID uuid) {
        Location location = new Location(uuid);
        location.setContinents(continentRepository.findByfk_campaign_uuid(uuid));
        location.setCountries(countryRepository.findByfk_campaign_uuid(uuid));
        location.setRegions(regionRepository.findByfk_campaign_uuid(uuid));
        location.setCities(cityRepository.findByfk_campaign_uuid(uuid));
        location.setPlaces(placeRepository.findByfk_campaign_uuid(uuid));
        location.setLandmarks(landmarkRepository.findByfk_campaign_uuid(uuid));
        return location;
    }

}
