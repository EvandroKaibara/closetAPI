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
class AccessoryControllerTest {
    private static final String ACCESSORY = """
            {"brand": "New Era", "type": "Cap", "color": "Black"}""";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReadUpdateDelete() throws Exception {
        String created = mockMvc.perform(post("/accessories").contentType(MediaType.APPLICATION_JSON).content(ACCESSORY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("New Era"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(created, "$.id");

        mockMvc.perform(get("/accessories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Cap"));

        mockMvc.perform(put("/accessories/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(ACCESSORY.replace("Cap", "Bucket hat")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.type").value("Bucket hat"));

        mockMvc.perform(get("/accessories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        mockMvc.perform(delete("/accessories/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/accessories/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidAccessory() throws Exception {
        mockMvc.perform(post("/accessories").contentType(MediaType.APPLICATION_JSON)
                        .content(ACCESSORY.replace("\"New Era\"", "\"\"")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/accessories/{id}", 0)).andExpect(status().isNotFound());
        mockMvc.perform(put("/accessories/{id}", 0).contentType(MediaType.APPLICATION_JSON).content(ACCESSORY))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/accessories/{id}", 0)).andExpect(status().isNotFound());
    }
}
