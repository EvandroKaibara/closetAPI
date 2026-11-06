package com.closet.closetapi.repositories;

import com.closet.closetapi.models.Outfit;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutfitRepository extends ListCrudRepository<Outfit, Integer> {
}
