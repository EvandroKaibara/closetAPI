package com.closet.closetapi.services;

import com.closet.closetapi.models.Hoodie;
import com.closet.closetapi.repositories.HoodieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HoodieService {
    @Autowired
    private HoodieRepository hoodieRepository;

    public List<Hoodie> findAll() {
        return hoodieRepository.findAll();
    }

    public Hoodie findById(Integer id) {
        return hoodieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hoodie " + id + " not found"));
    }

    public Hoodie create(Hoodie hoodie) {
        hoodie.setId(null);
        return hoodieRepository.save(hoodie);
    }

    public Hoodie update(Integer id, Hoodie hoodie) {
        findById(id);
        hoodie.setId(id);
        return hoodieRepository.save(hoodie);
    }

    public void delete(Integer id) {
        hoodieRepository.delete(findById(id));
    }
}
