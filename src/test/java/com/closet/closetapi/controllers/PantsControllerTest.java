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
class PantsControllerTest {
    private static final String PANTS = """
            {"brand": "Levi's", "size": "40", "color": "Blue", "fit": "Slim"}""";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReadUpdateDelete() throws Exception {
        String created = mockMvc.perform(post("/pants").contentType(MediaType.APPLICATION_JSON).content(PANTS))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Levi's"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(created, "$.id");

        mockMvc.perform(get("/pants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fit").value("Slim"));

        mockMvc.perform(put("/pants/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(PANTS.replace("Slim", "Baggy")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fit").value("Baggy"));

        mockMvc.perform(get("/pants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        mockMvc.perform(delete("/pants/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/pants/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidPants() throws Exception {
        mockMvc.perform(post("/pants").contentType(MediaType.APPLICATION_JSON)
                        .content(PANTS.replace("\"Blue\"", "\"\"")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/pants/{id}", 0)).andExpect(status().isNotFound());
        mockMvc.perform(put("/pants/{id}", 0).contentType(MediaType.APPLICATION_JSON).content(PANTS))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/pants/{id}", 0)).andExpect(status().isNotFound());
    }
}
