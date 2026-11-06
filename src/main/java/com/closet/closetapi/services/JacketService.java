package com.closet.closetapi.services;

import com.closet.closetapi.models.Jacket;
import com.closet.closetapi.repositories.JacketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JacketService {
    @Autowired
    private JacketRepository jacketRepository;

    public List<Jacket> findAll() {
        return jacketRepository.findAll();
    }

    public Jacket findById(Integer id) {
        return jacketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jacket " + id + " not found"));
    }

    public Jacket create(Jacket jacket) {
        jacket.setId(null);
        return jacketRepository.save(jacket);
    }

    public Jacket update(Integer id, Jacket jacket) {
        findById(id);
        jacket.setId(id);
        return jacketRepository.save(jacket);
    }

    public void delete(Integer id) {
        jacketRepository.delete(findById(id));
    }
}
