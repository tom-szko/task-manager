package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.szkopinski.todoo.service.AccountService.encodePassword;

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

  @DeleteMapping("/{accountId}")
  @ApiOperation(value = "Removes user account")
  public ResponseEntity deleteAccount(@PathVariable("accountId") String accountId) {
    accountService.deleteById(Integer.valueOf(accountId));
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Updates an account", notes = "Updates and account")
  public ResponseEntity updateAccount(@PathVariable("accountId") String accountId, @Valid @RequestBody Account updatedAccount) {
    try {
      return accountService.findById(Integer.valueOf(accountId))
              .map(account -> {
                account.setUserName(updatedAccount.getUserName());
                account.setPassword(encodePassword(updatedAccount.getPassword()));
                account.setEmail(updatedAccount.getEmail());
                return ResponseEntity.ok(account);
              }).orElse(ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
