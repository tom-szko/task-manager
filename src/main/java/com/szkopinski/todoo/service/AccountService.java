package com.szkopinski.todoo.service;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  public Iterable<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  public Optional<Account> getAccountByUserName(String username) {
    return accountRepository.findByUserName(username);
  }

  public Optional<Account> getAccountById(int accountId) {
    return accountRepository.findById(accountId);
  }

  @Transactional
  public Account addAccount(Account account) {
    String password = account.getPassword();
    account.setPassword(encodePassword(password));
    return accountRepository.save(account);
  }

  @Transactional
  public void deleteAccount(int accountId) {
    accountRepository.deleteById(accountId);
  }

  @Transactional
  public Account updateAccount(int accountId, Account updatedAccount) {
    return accountRepository.findById(accountId)
            .map(account -> {
              account.setUserName(updatedAccount.getUserName());
              account.setPassword(updatedAccount.getPassword());
              account.setEmail(updatedAccount.getEmail());
              return account;
            }).orElse(null);
  }

  public UserDetailsService getUser() {
    return userName -> {
      Optional<Account> account = accountRepository.findByUserName(userName);
      if (account.isPresent()) {
        return new User(account.get().getUserName(), account.get().getPassword(), true, true, true, true, AuthorityUtils.createAuthorityList(
                "USER"));
      } else {
        throw new UsernameNotFoundException("Could not find the account '" + userName + "'");
      }
    };
  }
}
