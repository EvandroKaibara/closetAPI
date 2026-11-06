package com.closet.closetapi.repositories;

import com.closet.closetapi.models.Sneaker;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SneakerRepository extends ListCrudRepository<Sneaker, Integer> {
}
