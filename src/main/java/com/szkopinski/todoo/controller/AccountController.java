package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountController {

  private AccountRepository accountRepository;
  private PasswordEncoder passwordEncoder;

  public AccountController(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping
  @ApiOperation(value = "Finds all registered accounts", response = Account[].class)
  public ResponseEntity<Iterable<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountRepository.findAll());
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Finds single account by id", response = Account.class)
  public ResponseEntity<Account> getAccount(@PathVariable("id") String accountId) {
    return ResponseEntity.of(accountRepository.findById(Integer.valueOf(accountId)));
  }

  @PostMapping
  @ApiOperation(value = "Adds a new user account", response = Account.class)
  public ResponseEntity<Account> addAccount(Account account) {
    String password = account.getPassword();
    account.setPassword(passwordEncoder.encode(password));
    return ResponseEntity.ok(accountRepository.save(account));
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Removes user account")
  public ResponseEntity removeAccount(@PathVariable("id") String accountId) {
    accountRepository.deleteById(Integer.valueOf(accountId));
    return ResponseEntity.noContent().build();
  }
}
