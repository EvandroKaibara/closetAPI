package com.closet.closetapi.repositories;

import com.closet.closetapi.models.Jacket;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JacketRepository extends ListCrudRepository<Jacket, Integer> {
}
