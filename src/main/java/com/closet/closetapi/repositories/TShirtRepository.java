package com.closet.closetapi.repositories;

import com.closet.closetapi.models.TShirt;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TShirtRepository extends ListCrudRepository<TShirt, Integer> {
}
