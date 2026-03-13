package com.mcommings.campaigner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public abstract class BaseControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    protected MediaType json = MediaType.APPLICATION_JSON;

    protected ResultActions get(String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(json)
        );
    }

    protected ResultActions post(String url, Object body) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(json)
                        .content(toJson(body))
        );
    }

    protected ResultActions put(String url, Object body) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .contentType(json)
                        .content(toJson(body))
        );
    }

    protected ResultActions delete(String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .contentType(json)
        );
    }

    protected String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}
