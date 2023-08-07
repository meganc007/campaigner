package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Race;
import com.mcommings.campaigner.services.RaceService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;

import static java.util.Objects.isNull;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(RaceController.class)
public class RaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RaceService raceService;

    private Race testRace;

    @BeforeEach
    public void setUp() {
        // Create a test Race object
        testRace = new Race(1, "Test Race", "Description", true);
    }

    @Test
    public void contextLoads() {
        boolean expected = false;
        boolean actual = isNull(mockMvc);

        Assertions.assertEquals(expected, actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void whenValidEndpointUsed_testGetRaces_ReturnsListofRaces() throws Exception {
        Mockito.when(raceService.getRaces()).thenReturn(Arrays.asList(testRace));

        mockMvc.perform(get("/api/people/races"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testRace.getId()))
                .andExpect(jsonPath("$[0].name").value(testRace.getName()))
                .andExpect(jsonPath("$[0].description").value(testRace.getDescription()))
                .andExpect(jsonPath("$[0]._exotic").value(testRace.is_exotic()));
    }

    @Test
    public void whenInvalidEndpointUsed_testGetRaces_Returns404NotFound() throws Exception {
        mockMvc.perform(get("/api/people/race/"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenValidRequestUsed_testSaveRace_returns200IsOk() throws Exception {
        Mockito.doNothing().when(raceService).saveRace(testRace);

        mockMvc.perform(post("/api/people/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Test Race\", \"description\": \"Description\", \"is_exotic\": true }"))
                .andExpect(status().isOk());
    }
    @Test
    public void whenInvalidRequestUsed_testSaveRace_returns500InternalServerError() throws Exception {
         try {
            mockMvc.perform(post("/api/people/races")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"description\": \"Description\", \"is_exotic\": true }"));
        } catch (Exception exception) {
            Assertions.assertEquals("Race name cannot be null or empty.", exception.getLocalizedMessage());
        }
    }

}
