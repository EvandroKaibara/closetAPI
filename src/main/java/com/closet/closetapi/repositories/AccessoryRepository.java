package com.closet.closetapi.repositories;

import com.closet.closetapi.models.Accessory;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessoryRepository extends ListCrudRepository<Accessory, Integer> {
}
