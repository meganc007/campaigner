package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.CountryController;
import com.mcommings.campaigner.modules.locations.dtos.countries.CreateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.UpdateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.ViewCountryDTO;
import com.mcommings.campaigner.modules.locations.services.CountryService;
import com.mcommings.campaigner.setup.locations.factories.LocationsTestDataFactory;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
public class CountryControllerTest extends BaseControllerTest {

    @MockitoBean
    private CountryService countryService;

    //GET all
    @Test
    void getCountries_returnsList() throws Exception {

        when(countryService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewCountryDTO()));

        get("/api/countries")
                .andExpect(status().isOk());

        verify(countryService).getAll();
    }

    //GET by id
    @Test
    void getCountry_returnsCountry() throws Exception {

        ViewCountryDTO dto = LocationsTestDataFactory.viewCountryDTO();

        when(countryService.getById(1)).thenReturn(dto);

        get("/api/countries/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by ContinentId
    @Test
    void getCountriesByContinentId_returnsCountries() throws Exception {

        when(countryService.getCountriesByContinentId(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCountryDTO()));

        get("/api/countries/continent/" + LocationsTestConstants.CONTINENT_ID)
                .andExpect(status().isOk());

        verify(countryService)
                .getCountriesByContinentId(LocationsTestConstants.CONTINENT_ID);
    }

    //GET by GovernmentId
    @Test
    void getCountriesByGovernmentId_returnsCountries() throws Exception {

        when(countryService.getCountriesByGovernmentId(LocationsTestConstants.GOVERNMENT_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCountryDTO()));

        get("/api/countries/government/" + LocationsTestConstants.GOVERNMENT_ID)
                .andExpect(status().isOk());

        verify(countryService)
                .getCountriesByGovernmentId(LocationsTestConstants.GOVERNMENT_ID);
    }

    //GET by CampaignUUID
    @Test
    void getCountriesByCampaignUUID_returnsCountries() throws Exception {

        when(countryService.getCountriesByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCountryDTO()));

        get("/api/countries/campaign/" + LocationsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(countryService)
                .getCountriesByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createCountry_returnsSaved() throws Exception {

        CreateCountryDTO create =
                LocationsTestDataFactory.createCountryDTO();

        ViewCountryDTO response =
                LocationsTestDataFactory.viewCountryDTO();

        when(countryService.create(any())).thenReturn(response);

        post("/api/countries", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateCountry_returnsUpdated() throws Exception {

        UpdateCountryDTO update =
                LocationsTestDataFactory.updateCountryDTO();

        ViewCountryDTO response =
                LocationsTestDataFactory.viewCountryDTO();

        when(countryService.update(any())).thenReturn(response);

        put("/api/countries", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteCountry_returnsOk() throws Exception {

        delete("/api/countries/1")
                .andExpect(status().isOk());

        verify(countryService).delete(1);
    }
}
