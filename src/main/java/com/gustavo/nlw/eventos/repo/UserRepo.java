package com.gustavo.nlw.eventos.repo;

import com.gustavo.nlw.eventos.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
}
