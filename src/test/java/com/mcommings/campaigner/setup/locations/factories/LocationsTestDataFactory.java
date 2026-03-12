package com.mcommings.campaigner.setup.locations.factories;

import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.CreateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.UpdateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.ViewCountryDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.setup.locations.builders.ContinentBuilder;
import com.mcommings.campaigner.setup.locations.builders.CountryBuilder;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

public class LocationsTestDataFactory {

    public static Continent continent() {
        return ContinentBuilder.aContinent().build();
    }

    public static ViewContinentDTO viewContinentDTO() {
        ViewContinentDTO dto = new ViewContinentDTO();
        dto.setId(LocationsTestConstants.CONTINENT_ID);
        dto.setName(LocationsTestConstants.CONTINENT_NAME);
        dto.setDescription(LocationsTestConstants.CONTINENT_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    public static CreateContinentDTO createContinentDTO() {
        CreateContinentDTO dto = new CreateContinentDTO();
        dto.setName(LocationsTestConstants.CONTINENT_NAME);
        dto.setDescription(LocationsTestConstants.CONTINENT_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    public static UpdateContinentDTO updateContinentDTO() {
        UpdateContinentDTO dto = new UpdateContinentDTO();
        dto.setId(LocationsTestConstants.CONTINENT_ID);
        dto.setName(LocationsTestConstants.CONTINENT_NAME);
        dto.setDescription(LocationsTestConstants.CONTINENT_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    public static Country country() {
        return CountryBuilder.aCountry().build();
    }

    public static ViewCountryDTO viewCountryDTO() {
        ViewCountryDTO dto = new ViewCountryDTO();
        dto.setId(LocationsTestConstants.COUNTRY_ID);
        dto.setName(LocationsTestConstants.COUNTRY_NAME);
        dto.setDescription(LocationsTestConstants.COUNTRY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setContinentId(LocationsTestConstants.CONTINENT_ID);
        dto.setGovernmentId(LocationsTestConstants.GOVERNMENT_ID);
        return dto;
    }

    public static CreateCountryDTO createCountryDTO() {
        CreateCountryDTO dto = new CreateCountryDTO();
        dto.setName(LocationsTestConstants.COUNTRY_NAME);
        dto.setDescription(LocationsTestConstants.COUNTRY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setContinentId(LocationsTestConstants.CONTINENT_ID);
        dto.setGovernmentId(LocationsTestConstants.GOVERNMENT_ID);
        return dto;
    }

    public static UpdateCountryDTO updateCountryDTO() {
        UpdateCountryDTO dto = new UpdateCountryDTO();
        dto.setId(LocationsTestConstants.COUNTRY_ID);
        dto.setName(LocationsTestConstants.COUNTRY_NAME);
        dto.setDescription(LocationsTestConstants.COUNTRY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

}