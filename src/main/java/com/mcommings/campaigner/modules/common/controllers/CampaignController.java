package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.CampaignDTO;
import com.mcommings.campaigner.modules.common.services.interfaces.ICampaign;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/campaigns")
public class CampaignController {

    private final ICampaign campaignService;

    @GetMapping
    public List<CampaignDTO> getCampaigns() {
        return campaignService.getCampaigns();
    }

    @GetMapping(path = "/{uuid}")
    public CampaignDTO getCampaign(@PathVariable("uuid") UUID uuid) {

        return campaignService.getCampaign(uuid).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveCampaign(@Valid @RequestBody CampaignDTO campaign) {
        campaignService.saveCampaign(campaign);
    }

    @DeleteMapping(path = "{uuid}")
    public void deleteCampaign(@PathVariable("uuid") UUID uuid) {
        campaignService.deleteCampaign(uuid);
    }
    
    @PutMapping(path = "{uuid}")
    public void updateCampaign(@PathVariable("uuid") UUID uuid, @RequestBody CampaignDTO campaign) {
        campaignService.updateCampaign(uuid, campaign);
    }
}
