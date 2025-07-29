package com.byllameister.modelstore.repositories;

import com.byllameister.modelstore.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
