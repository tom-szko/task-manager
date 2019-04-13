package com.szkopinski.todoo.service;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private AccountRepository accountRepository;

  @Autowired
  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public UserDetailsService getUser() {
    return userName -> {
      Account account = accountRepository.findByUserName(userName);
      if (account != null) {
        return new User(account.getUserName(), account.getPassword(), true, true, true, true, AuthorityUtils.createAuthorityList(
            "USER"));
      } else {
        throw new UsernameNotFoundException("Could not find the account '" + userName + "'");
      }
    };
  }
}
