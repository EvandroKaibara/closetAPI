package com.closet.closetapi.services;

import com.closet.closetapi.models.TShirt;
import com.closet.closetapi.repositories.TShirtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TShirtService {
    @Autowired
    private TShirtRepository tShirtRepository;

    public List<TShirt> findAll() {
        return tShirtRepository.findAll();
    }

    public TShirt findById(Integer id) {
        return tShirtRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "T-shirt " + id + " not found"));
    }

    public TShirt create(TShirt tShirt) {
        tShirt.setId(null);
        return tShirtRepository.save(tShirt);
    }

    public TShirt update(Integer id, TShirt tShirt) {
        findById(id);
        tShirt.setId(id);
        return tShirtRepository.save(tShirt);
    }

    public void delete(Integer id) {
        tShirtRepository.delete(findById(id));
    }
}
