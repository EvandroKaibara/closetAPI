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
class JacketControllerTest {
    private static final String JACKET = """
            {"brand": "The North Face", "name": "Nuptse", "purchasePrice": 1899.00,
             "size": "L", "color": "Black"}""";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReadUpdateDelete() throws Exception {
        String created = mockMvc.perform(post("/jackets").contentType(MediaType.APPLICATION_JSON).content(JACKET))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("The North Face"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(created, "$.id");

        mockMvc.perform(get("/jackets/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nuptse"));

        mockMvc.perform(put("/jackets/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(JACKET.replace("\"L\"", "\"XL\"")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.size").value("XL"));

        mockMvc.perform(get("/jackets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        mockMvc.perform(delete("/jackets/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/jackets/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidJacket() throws Exception {
        mockMvc.perform(post("/jackets").contentType(MediaType.APPLICATION_JSON)
                        .content(JACKET.replace("\"Nuptse\"", "\"\"").replace("1899.00", "-1")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/jackets/{id}", 0)).andExpect(status().isNotFound());
        mockMvc.perform(put("/jackets/{id}", 0).contentType(MediaType.APPLICATION_JSON).content(JACKET))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/jackets/{id}", 0)).andExpect(status().isNotFound());
    }
}
