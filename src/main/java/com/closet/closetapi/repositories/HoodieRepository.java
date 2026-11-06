package com.closet.closetapi.repositories;

import com.closet.closetapi.models.Hoodie;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoodieRepository extends ListCrudRepository<Hoodie, Integer> {
}
