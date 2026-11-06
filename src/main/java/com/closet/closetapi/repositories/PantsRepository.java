package com.closet.closetapi.repositories;

import com.closet.closetapi.models.Pants;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PantsRepository extends ListCrudRepository<Pants, Integer> {
}
