package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Climate;
import com.mcommings.campaigner.models.repositories.IClimateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ClimateTest {

    @Mock
    private IClimateRepository climateRepository;

    @InjectMocks
    private ClimateService climateService;

    @Test
    public void whenThereAreClimates_getClimates_ReturnsClimates() {
        List<Climate> climates = new ArrayList<>();
        climates.add(new Climate(1, "Climate 1", "Description 1"));
        climates.add(new Climate(2, "Climate 2", "Description 2"));

        when(climateRepository.findAll()).thenReturn(climates);

        List<Climate> result = climateService.getClimates();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(climates, result);
    }

    @Test
    public void whenThereAreNoClimates_getClimates_ReturnsNothing() {
        List<Climate> climates = new ArrayList<>();
        when(climateRepository.findAll()).thenReturn(climates);

        List<Climate> result = climateService.getClimates();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(climates, result);
    }
}
