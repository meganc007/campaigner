package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Campaign;
import com.mcommings.campaigner.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/campaigns")
public class CampaignController {
    
    private final CampaignService campaignService;
    
    @Autowired
    public CampaignController (CampaignService campaignService) {
        this.campaignService = campaignService;
    }
    
    @GetMapping
    public List<Campaign> getCampaigns() {
        return campaignService.getCampaigns();
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
