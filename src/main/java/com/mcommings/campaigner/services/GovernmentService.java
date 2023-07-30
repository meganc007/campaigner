package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Government;
import com.mcommings.campaigner.models.IGovernmentRepository;
import com.mcommings.campaigner.interfaces.IGovernment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GovernmentService implements IGovernment {

    private final IGovernmentRepository governmentRepository;

    @Autowired
    public GovernmentService (IGovernmentRepository governmentRepository) {this.governmentRepository = governmentRepository;}

    @Override
    public List<Government> getGovernments() {
        return governmentRepository.findAll();
    }
}
