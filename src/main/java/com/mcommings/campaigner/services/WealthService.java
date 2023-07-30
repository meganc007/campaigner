package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IWealth;
import com.mcommings.campaigner.models.IWealthRepository;
import com.mcommings.campaigner.models.Wealth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WealthService implements IWealth {

    private final IWealthRepository wealthRepository;

    @Autowired
    public WealthService(IWealthRepository wealthRepository) {this.wealthRepository = wealthRepository;}
    @Override
    public List<Wealth> getWealth() {
        return wealthRepository.findAll();
    }
}
