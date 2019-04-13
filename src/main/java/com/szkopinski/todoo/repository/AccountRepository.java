package com.szkopinski.todoo.repository;

import com.szkopinski.todoo.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

  Account findByUserName(String userName);
}
