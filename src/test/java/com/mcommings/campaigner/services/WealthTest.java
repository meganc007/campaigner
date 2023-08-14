package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Wealth;
import com.mcommings.campaigner.models.repositories.IWealthRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class WealthTest {

    @Mock
    private IWealthRepository wealthRepository;

    @InjectMocks
    private WealthService wealthService;

    @Test
    public void whenThereAreWealth_getWealth_ReturnsWealth() {
        List<Wealth> wealthList = new ArrayList<>();
        wealthList.add(new Wealth(1, "Wealth 1"));
        wealthList.add(new Wealth(2, "Wealth 2"));
        when(wealthRepository.findAll()).thenReturn(wealthList);

        List<Wealth> result = wealthService.getWealth();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(wealthList, result);
    }

    @Test
    public void whenThereAreNoWealth_getWealth_ReturnsNothing() {
        List<Wealth> wealthList = new ArrayList<>();
        when(wealthRepository.findAll()).thenReturn(wealthList);

        List<Wealth> result = wealthService.getWealth();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(wealthList, result);

    }
}
