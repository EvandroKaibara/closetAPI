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
class SneakerControllerTest {
    private static final String SNEAKER = """
            {"brand": "Nike", "name": "Air Max 90", "purchasePrice": 799.90,
             "size": "42", "color": "White", "style": "Casual"}""";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReadUpdateDelete() throws Exception {
        String created = mockMvc.perform(post("/sneakers").contentType(MediaType.APPLICATION_JSON).content(SNEAKER))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Nike"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(created, "$.id");

        mockMvc.perform(get("/sneakers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Air Max 90"));

        mockMvc.perform(put("/sneakers/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(SNEAKER.replace("White", "Black")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value("Black"));

        mockMvc.perform(get("/sneakers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        mockMvc.perform(delete("/sneakers/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/sneakers/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidSneaker() throws Exception {
        mockMvc.perform(post("/sneakers").contentType(MediaType.APPLICATION_JSON)
                        .content(SNEAKER.replace("\"Nike\"", "\"\"").replace("799.90", "-1")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/sneakers/{id}", 0)).andExpect(status().isNotFound());
        mockMvc.perform(put("/sneakers/{id}", 0).contentType(MediaType.APPLICATION_JSON).content(SNEAKER))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/sneakers/{id}", 0)).andExpect(status().isNotFound());
    }
}
