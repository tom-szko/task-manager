package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountController {

  private AccountRepository accountService;

  public AccountController(AccountRepository accountService) {
    this.accountService = accountService;
  }

  @GetMapping
  @ApiOperation(value = "Finds all registered accounts", response = Account[].class)
  public ResponseEntity<Iterable<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountService.findAll());
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Finds single account by id", response = Account.class)
  public ResponseEntity<Account> getAccount(@PathVariable("id") String accountId) {
    return ResponseEntity.of(accountService.findById(Integer.valueOf(accountId)));
  }

  @PostMapping
  @ApiOperation(value = "Adds a new user account", response = Account.class)
  public ResponseEntity<Account> addAccount(@RequestBody Account account) {
    return ResponseEntity.ok(accountService.save(account));
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Removes user account")
  public ResponseEntity deleteAccount(@PathVariable("id") String accountId) {
    accountService.deleteById(Integer.valueOf(accountId));
    return ResponseEntity.noContent().build();
  }
}
