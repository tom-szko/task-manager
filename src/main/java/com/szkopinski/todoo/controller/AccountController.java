package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
@Validated
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @ApiOperation(value = "Finds all registered accounts", response = Account[].class)
    public ResponseEntity<Iterable<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{userName}")
    @ApiOperation(value = "Finds single account by userName", response = Account.class)
    public ResponseEntity<Account> getAccount(@PathVariable("userName") String userName) {
        return ResponseEntity.of(accountService.getAccountByUserName(userName));
    }

    @PostMapping
    @ApiOperation(value = "Adds a new user account", response = Account.class)
    public ResponseEntity<Account> addAccount(@Valid @RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.addAccount(account));
    }

    @DeleteMapping("/{accountId}")
    @ApiOperation(value = "Removes user account")
    public ResponseEntity deleteAccount(@Min(1) @PathVariable("accountId") int accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{accountId}")
    @ApiOperation(value = "Updates an account", notes = "Updates and account")
    public ResponseEntity updateAccount(@Min(1) @PathVariable("accountId") int accountId, @Valid @RequestBody Account updatedAccount) {
        try {
            Account newAccount = accountService.updateAccount(accountId, updatedAccount);
            if (newAccount == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(newAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
