package com.mcommings.campaigner.controllers.common;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.common.controllers.CampaignController;
import com.mcommings.campaigner.modules.common.dtos.campaigns.CreateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.UpdateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.ViewCampaignDTO;
import com.mcommings.campaigner.modules.common.services.CampaignService;
import com.mcommings.campaigner.setup.common.factories.CommonTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CampaignController.class)
public class CampaignControllerTest extends BaseControllerTest {

    @MockitoBean
    CampaignService campaignService;

    UUID uuid = UUID.randomUUID();

    //GET all
    @Test
    void getCampaigns_returnsList() throws Exception {

        when(campaignService.getAll())
                .thenReturn(List.of(CommonTestDataFactory.viewCampaignDTO()));

        get("/api/campaigns")
                .andExpect(status().isOk());

        verify(campaignService).getAll();
    }

    //GET by id
    @Test
    void getCampaign_returnsCampaign() throws Exception {

        ViewCampaignDTO dto = CommonTestDataFactory.viewCampaignDTO();

        when(campaignService.getById(uuid)).thenReturn(dto);

        get("/api/campaigns/" + uuid)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(dto.getUuid().toString()));
    }

    //POST
    @Test
    void createCampaign_returnsSaved() throws Exception {

        CreateCampaignDTO create =
                CommonTestDataFactory.createCampaignDTO();

        ViewCampaignDTO response =
                CommonTestDataFactory.viewCampaignDTO();

        when(campaignService.create(any())).thenReturn(response);

        post("/api/campaigns", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateCampaign_returnsUpdated() throws Exception {

        UpdateCampaignDTO update =
                CommonTestDataFactory.updateCampaignDTO();

        ViewCampaignDTO response =
                CommonTestDataFactory.viewCampaignDTO();

        when(campaignService.update(any())).thenReturn(response);

        put("/api/campaigns", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteCampaign_returnsOk() throws Exception {

        delete("/api/campaigns/" + uuid)
                .andExpect(status().isOk());

        verify(campaignService).delete(uuid);
    }
}
