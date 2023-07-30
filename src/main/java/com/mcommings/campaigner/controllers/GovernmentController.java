package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Government;
import com.mcommings.campaigner.services.GovernmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/governments")
public class GovernmentController {

    private final GovernmentService governmentService;

    @Autowired
    public GovernmentController (GovernmentService governmentService) {this.governmentService = governmentService;}

    @GetMapping
    public List<Government> getGovernments() {
        return governmentService.getGovernments();
    }
}
