package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/campaigns")
public class CampaignController {
    
    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public List<Campaign> getCampaigns() {
        return campaignService.getCampaigns();
    }

    @GetMapping(path = "/{uuid}")
    public Campaign getCampaign(@PathVariable("uuid") UUID uuid) {
        return campaignService.getCampaign(uuid);
    }

    @PostMapping
    public void saveCampaign(@RequestBody Campaign campaign) {
        campaignService.saveCampaign(campaign);
    }

    @DeleteMapping(path = "{uuid}")
    public void deleteCampaign(@PathVariable("uuid") UUID uuid) {
        campaignService.deleteCampaign(uuid);
    }
    
    @PutMapping(path = "{uuid}")
    public void updateCampaign(@PathVariable("uuid") UUID uuid, @RequestBody Campaign campaign) {
        campaignService.updateCampaign(uuid, campaign);
    }
}
