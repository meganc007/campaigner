package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Government;
import com.mcommings.campaigner.models.repositories.IGovernmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class GovernmentTest {

    @Mock
    private IGovernmentRepository governmentRepository;

    @InjectMocks
    private GovernmentService governmentService;

    @Test
    public void whenThereAreGovernments_getGovernments_ReturnsGovernments() {
        List<Government> governments = new ArrayList<>();
        governments.add(new Government(1, "Government 1", "Description 1"));
        governments.add(new Government(2, "Government 2", "Description 2"));
        when(governmentRepository.findAll()).thenReturn(governments);

        List<Government> result = governmentService.getGovernments();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(governments, result);
    }

    @Test
    public void whenThereAreNoGovernments_getGovernments_ReturnsNothing() {
        List<Government> governments = new ArrayList<>();
        when(governmentRepository.findAll()).thenReturn(governments);

        List<Government> result = governmentService.getGovernments();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(governments, result);
    }
}
