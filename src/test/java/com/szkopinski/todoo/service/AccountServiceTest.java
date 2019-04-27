package com.szkopinski.todoo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private AccountRepository repository;

  @Mock
  private PasswordEncoder passwordEncoder;

  private AccountService accountService;

  @BeforeEach
  void setUp() {
    accountService = new AccountService(repository, passwordEncoder);
  }

  @Test
  @DisplayName("Should return all existing accounts")
  void shouldReturnAllExistingAccounts() {
    //given
    List<Account> accounts = new ArrayList<>();
    Account account1 = new Account("John Doe", "password", "john@doe.com");
    Account account2 = new Account("Claire Jenkins", "pass123", "claire@email.com");
    accounts.add(account1);
    accounts.add(account2);

    when(repository.findAll()).thenReturn(accounts);

    //when
    Iterable<Account> result = accountService.getAllAccounts();

    //then
    assertEquals(accounts, result);
    verify(repository).findAll();
  }

  @Test
  @DisplayName("Should return existing account with given Id number")
  void shouldReturnAccountWithGivenId() {
    //given
    int accountId = 1;
    Account account = new Account(accountId, "John Doe", "password", "john@doe.com");
    when(repository.findById(accountId)).thenReturn(Optional.of(account));

    //when
    Optional<Account> result = accountService.getAccountById(accountId);

    //then
    assertEquals(account, result.get());
    verify(repository).findById(accountId);
  }

  @Test
  @DisplayName("Should return existing account with given user name")
  void shouldReturnAccountWithGivenUserName() {
    //given
    String userName = "John Doe";
    Account account = new Account(userName, "password", "john@doe.com");
    when(repository.findByUserName("John Doe")).thenReturn(account);

    //when
    Account result = accountService.getAccountByUserName(userName);

    //then
    assertEquals(account, result);
    verify(repository).findByUserName(userName);
  }

  @Test
  @DisplayName("Should return added account")
  void shouldReturnAddedAccount() {
    //given
    Account account = new Account("John Doe", "password", "john@doe.com");
    when(repository.save(account)).thenReturn(account);

    //when
    Account result = accountService.addAccount(account);

    //then
    assertEquals(account, result);
    verify(repository).save(account);
  }

  @Test
  @DisplayName("Should update existing account with given id number")
  void shouldUpdateExistingAccount() {
    //given
    int accountId = 1;
    Account account = new Account(accountId, "John Doe", "password", "john@doe.com");
    Account updatedAccount = new Account(accountId, "John P. Doe", "updatedPassword", "john@doe2.com");
    when(repository.findById(accountId)).thenReturn(Optional.of(account));

    //when

    Account result = accountService.updateAccount(accountId, updatedAccount);

    //then
    assertEquals(account, result);
    verify(repository).findById(accountId);
  }

  @Test
  @DisplayName("Should delete existing account with given id number")
  void shouldDeleteExistingAccount() {
    //given
    int accountId = 1;
    doNothing().when(repository).deleteById(accountId);

    //when
    accountService.deleteAccount(accountId);

    //then
    verify(repository).deleteById(accountId);
  }
}
