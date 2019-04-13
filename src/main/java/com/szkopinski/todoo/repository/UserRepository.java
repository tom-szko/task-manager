package com.szkopinski.todoo.repository;

import com.szkopinski.todoo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  User findByUserName(String userName);
}
