package com.closet.closetapi.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OutfitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Integer createPiece(String path, String json) throws Exception {
        String body = mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private Integer createSneaker() throws Exception {
        return createPiece("/sneakers", """
                {"brand": "Nike", "name": "Air Max 90", "purchasePrice": 799.90,
                 "size": "42", "color": "White", "style": "Casual"}""");
    }

    private Integer createAccessory() throws Exception {
        return createPiece("/accessories", """
                {"brand": "New Era", "type": "Cap", "color": "Black"}""");
    }

    private Integer createOutfit(Integer sneakerId, Integer accessoryId) throws Exception {
        String body = mockMvc.perform(post("/outfits").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Weekend\", \"sneakerId\": %d, \"accessoryIds\": [%d]}"
                                .formatted(sneakerId, accessoryId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Weekend"))
                .andExpect(jsonPath("$.sneaker.id").value(sneakerId))
                .andExpect(jsonPath("$.accessories[0].id").value(accessoryId))
                .andExpect(jsonPath("$.tshirt").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    @Test
    void createReadUpdateDelete() throws Exception {
        Integer sneakerId = createSneaker();
        Integer accessoryId = createAccessory();
        Integer id = createOutfit(sneakerId, accessoryId);

        mockMvc.perform(get("/outfits/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sneaker.brand").value("Nike"))
                .andExpect(jsonPath("$.accessories[0].type").value("Cap"));

        mockMvc.perform(put("/outfits/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Gym\", \"accessoryIds\": []}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gym"))
                .andExpect(jsonPath("$.sneaker").doesNotExist())
                .andExpect(jsonPath("$.accessories").isEmpty());

        mockMvc.perform(get("/outfits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        mockMvc.perform(delete("/outfits/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/outfits/{id}", id)).andExpect(status().isNotFound());
        // Pieces outlive the outfit
        mockMvc.perform(get("/sneakers/{id}", sneakerId)).andExpect(status().isOk());
    }

    // Runs outside the test transaction so each request commits, as in production,
    // and the assertions see the database ON DELETE rules instead of the session cache
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void deletingPiecesRemovesThemFromOutfit() throws Exception {
        Integer sneakerId = createSneaker();
        Integer accessoryId = createAccessory();
        Integer id = createOutfit(sneakerId, accessoryId);
        try {
            mockMvc.perform(delete("/sneakers/{id}", sneakerId)).andExpect(status().isNoContent());
            mockMvc.perform(delete("/accessories/{id}", accessoryId)).andExpect(status().isNoContent());

            mockMvc.perform(get("/outfits/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sneaker").doesNotExist())
                    .andExpect(jsonPath("$.accessories").isEmpty());
        } finally {
            mockMvc.perform(delete("/outfits/{id}", id));
        }
    }

    @Test
    void rejectsInvalidOutfit() throws Exception {
        mockMvc.perform(post("/outfits").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/outfits").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Weekend\", \"sneakerId\": 0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Sneaker 0 does not exist"));
        mockMvc.perform(post("/outfits").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Weekend\", \"accessoryIds\": [0]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Accessories [0] do not exist"));
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/outfits/{id}", 0)).andExpect(status().isNotFound());
        mockMvc.perform(put("/outfits/{id}", 0).contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Gym\"}"))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/outfits/{id}", 0)).andExpect(status().isNotFound());
    }
}
