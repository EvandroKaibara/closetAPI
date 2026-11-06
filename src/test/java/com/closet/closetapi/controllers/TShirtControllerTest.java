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
class TShirtControllerTest {
    private static final String TSHIRT = """
            {"brand": "Adidas", "name": "Trefoil Tee", "purchasePrice": 149.90,
             "size": "M", "color": "Black", "fit": "Regular"}""";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReadUpdateDelete() throws Exception {
        String created = mockMvc.perform(post("/tshirts").contentType(MediaType.APPLICATION_JSON).content(TSHIRT))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Adidas"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(created, "$.id");

        mockMvc.perform(get("/tshirts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fit").value("Regular"));

        mockMvc.perform(put("/tshirts/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(TSHIRT.replace("Regular", "Oversized")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.fit").value("Oversized"));

        mockMvc.perform(get("/tshirts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        mockMvc.perform(delete("/tshirts/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/tshirts/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidTShirt() throws Exception {
        mockMvc.perform(post("/tshirts").contentType(MediaType.APPLICATION_JSON)
                        .content(TSHIRT.replace("\"Adidas\"", "\"\"").replace("149.90", "-1")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/tshirts/{id}", 0)).andExpect(status().isNotFound());
        mockMvc.perform(put("/tshirts/{id}", 0).contentType(MediaType.APPLICATION_JSON).content(TSHIRT))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/tshirts/{id}", 0)).andExpect(status().isNotFound());
    }
}
