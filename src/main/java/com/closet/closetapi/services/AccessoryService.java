package com.closet.closetapi.services;

import com.closet.closetapi.models.Accessory;
import com.closet.closetapi.repositories.AccessoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AccessoryService {
    @Autowired
    private AccessoryRepository accessoryRepository;

    public List<Accessory> findAll() {
        return accessoryRepository.findAll();
    }

    public Accessory findById(Integer id) {
        return accessoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Accessory " + id + " not found"));
    }

    public Accessory create(Accessory accessory) {
        accessory.setId(null);
        return accessoryRepository.save(accessory);
    }

    public Accessory update(Integer id, Accessory accessory) {
        findById(id);
        accessory.setId(id);
        return accessoryRepository.save(accessory);
    }

    public void delete(Integer id) {
        accessoryRepository.delete(findById(id));
    }
}
