package com.byllameister.modelstore.repositories;

import com.byllameister.modelstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
