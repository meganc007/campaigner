package com.mcommings.campaigner.setup.locations.factories;

import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.setup.locations.builders.ContinentBuilder;
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

}