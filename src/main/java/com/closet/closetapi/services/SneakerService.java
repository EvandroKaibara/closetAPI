package com.closet.closetapi.services;

import com.closet.closetapi.models.Sneaker;
import com.closet.closetapi.repositories.SneakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SneakerService {
    @Autowired
    private SneakerRepository sneakerRepository;

    public List<Sneaker> findAll() {
        return sneakerRepository.findAll();
    }

    public Sneaker findById(Integer id) {
        return sneakerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sneaker " + id + " not found"));
    }

    public Sneaker create(Sneaker sneaker) {
        sneaker.setId(null);
        return sneakerRepository.save(sneaker);
    }

    public Sneaker update(Integer id, Sneaker sneaker) {
        findById(id);
        sneaker.setId(id);
        return sneakerRepository.save(sneaker);
    }

    public void delete(Integer id) {
        sneakerRepository.delete(findById(id));
    }
}
