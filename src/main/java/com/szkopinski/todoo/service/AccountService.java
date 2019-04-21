package com.szkopinski.todoo.service;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private AccountRepository accountRepository;
  private static PasswordEncoder passwordEncoder;

  @Autowired
  public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Iterable<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  public Account getAccountByUserName(String username) {
    return accountRepository.findByUserName(username);
  }

  public Optional<Account> getAccountById(int accountId) {
    return accountRepository.findById(accountId);
  }

  public Account addAccount(Account account) {
    String password = account.getPassword();
    account.setPassword(encodePassword(password));
    return accountRepository.save(account);
  }

  public void deleteAccount(int accountId) {
    accountRepository.deleteById(accountId);
  }

  public static String encodePassword(String password) {
    return passwordEncoder.encode(password);
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
