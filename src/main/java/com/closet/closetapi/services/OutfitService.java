package com.closet.closetapi.services;

import com.closet.closetapi.models.Accessory;
import com.closet.closetapi.models.Outfit;
import com.closet.closetapi.models.OutfitRequest;
import com.closet.closetapi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OutfitService {
    @Autowired
    private OutfitRepository outfitRepository;
    @Autowired
    private SneakerRepository sneakerRepository;
    @Autowired
    private TShirtRepository tShirtRepository;
    @Autowired
    private PantsRepository pantsRepository;
    @Autowired
    private HoodieRepository hoodieRepository;
    @Autowired
    private JacketRepository jacketRepository;
    @Autowired
    private AccessoryRepository accessoryRepository;

    public List<Outfit> findAll() {
        return outfitRepository.findAll();
    }

    public Outfit findById(Integer id) {
        return outfitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Outfit " + id + " not found"));
    }

    public Outfit create(OutfitRequest request) {
        Outfit outfit = new Outfit();
        applyRequest(outfit, request);
        return outfitRepository.save(outfit);
    }

    public Outfit update(Integer id, OutfitRequest request) {
        Outfit outfit = findById(id);
        applyRequest(outfit, request);
        return outfitRepository.save(outfit);
    }

    public void delete(Integer id) {
        outfitRepository.delete(findById(id));
    }

    private void applyRequest(Outfit outfit, OutfitRequest request) {
        outfit.setName(request.name());
        outfit.setSneaker(findPiece(sneakerRepository, request.sneakerId(), "Sneaker"));
        outfit.setTshirt(findPiece(tShirtRepository, request.tshirtId(), "T-shirt"));
        outfit.setPants(findPiece(pantsRepository, request.pantsId(), "Pants"));
        outfit.setHoodie(findPiece(hoodieRepository, request.hoodieId(), "Hoodie"));
        outfit.setJacket(findPiece(jacketRepository, request.jacketId(), "Jacket"));
        outfit.setAccessories(findAccessories(request.accessoryIds()));
    }

    private static <T> T findPiece(ListCrudRepository<T, Integer> repository, Integer id, String label) {
        if (id == null) {
            return null;
        }
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, label + " " + id + " does not exist"));
    }

    private List<Accessory> findAccessories(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<Accessory> accessories = accessoryRepository.findAllById(ids);
        Set<Integer> foundIds = accessories.stream().map(Accessory::getId).collect(Collectors.toSet());
        List<Integer> missingIds = ids.stream().filter(id -> !foundIds.contains(id)).distinct().toList();
        if (!missingIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Accessories " + missingIds + " do not exist");
        }
        return accessories;
    }
}
