package com.szkopinski.todoo.repository;

import com.szkopinski.todoo.model.Account;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

  Optional<Account> findByUserName(String userName);
}
