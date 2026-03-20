package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.campaigns.CreateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.UpdateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.ViewCampaignDTO;
import com.mcommings.campaigner.modules.common.services.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping
    public List<ViewCampaignDTO> getCampaigns() {
        return campaignService.getAll();
    }

    @GetMapping(path = "/{uuid}")
    public ViewCampaignDTO getCampaign(@PathVariable UUID uuid) {

        return campaignService.getById(uuid);
    }

    @PostMapping
    public ViewCampaignDTO saveCampaign(@Valid @RequestBody CreateCampaignDTO campaign) {

        return campaignService.create(campaign);
    }

    @PutMapping
    public void updateCampaign(@Valid @RequestBody UpdateCampaignDTO campaign) {
        campaignService.update(campaign);
    }

    @DeleteMapping(path = "/{uuid}")
    public void deleteCampaign(@PathVariable UUID uuid) {
        campaignService.delete(uuid);
    }

}
