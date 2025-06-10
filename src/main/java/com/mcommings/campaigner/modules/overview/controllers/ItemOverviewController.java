package com.mcommings.campaigner.modules.overview.controllers;

import com.mcommings.campaigner.modules.overview.dtos.ItemOverviewDTO;
import com.mcommings.campaigner.modules.overview.services.interfaces.IItemOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/items-overview")
public class ItemOverviewController {

    private final IItemOverview itemService;

    @GetMapping(path = "/{uuid}")
    public ItemOverviewDTO getItemOverview(@PathVariable("uuid") UUID uuid) {
        return itemService.getItemOverview(uuid);
    }
}
