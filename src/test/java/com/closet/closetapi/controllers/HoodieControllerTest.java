package com.closet.closetapi.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HoodieControllerTest {
    private static final String HOODIE = """
            {"brand": "Champion", "color": "Gray"}""";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReadUpdateDelete() throws Exception {
        String created = mockMvc.perform(post("/hoodies").contentType(MediaType.APPLICATION_JSON).content(HOODIE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Champion"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(created, "$.id");

        mockMvc.perform(get("/hoodies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Gray"));

        mockMvc.perform(put("/hoodies/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(HOODIE.replace("Gray", "Navy")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value("Navy"));

        mockMvc.perform(get("/hoodies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        mockMvc.perform(delete("/hoodies/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/hoodies/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidHoodie() throws Exception {
        mockMvc.perform(post("/hoodies").contentType(MediaType.APPLICATION_JSON)
                        .content(HOODIE.replace("\"Champion\"", "\"\"")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/hoodies/{id}", 0)).andExpect(status().isNotFound());
        mockMvc.perform(put("/hoodies/{id}", 0).contentType(MediaType.APPLICATION_JSON).content(HOODIE))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/hoodies/{id}", 0)).andExpect(status().isNotFound());
    }
}
