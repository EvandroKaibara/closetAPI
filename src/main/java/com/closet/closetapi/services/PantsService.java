package com.closet.closetapi.services;

import com.closet.closetapi.models.Pants;
import com.closet.closetapi.repositories.PantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PantsService {
    @Autowired
    private PantsRepository pantsRepository;

    public List<Pants> findAll() {
        return pantsRepository.findAll();
    }

    public Pants findById(Integer id) {
        return pantsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pants " + id + " not found"));
    }

    public Pants create(Pants pants) {
        pants.setId(null);
        return pantsRepository.save(pants);
    }

    public Pants update(Integer id, Pants pants) {
        findById(id);
        pants.setId(id);
        return pantsRepository.save(pants);
    }

    public void delete(Integer id) {
        pantsRepository.delete(findById(id));
    }
}
