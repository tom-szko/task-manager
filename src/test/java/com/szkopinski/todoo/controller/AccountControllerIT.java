package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.szkopinski.todoo.helpers.TestHelpers.convertToJson;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountControllerIT {

  private static final String URL_TEMPLATE = "/api/accounts/";
  private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AccountService accountService;

  @Test
  @DisplayName("Should retrieve all accounts present in the database")
  @WithMockUser
  void shouldReturnAllAccounts() throws Exception {
    //given
    Iterable<Account> accounts = accountService.getAllAccounts();
    String accountsAsJson = convertToJson(accounts);
    //when
    mockMvc
        .perform(get(URL_TEMPLATE))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(accountsAsJson));
  }

  @Test
  @DisplayName("Should retrieve single account with given id")
  @WithMockUser
  void shouldReturnAccountWithGivenId() throws Exception {
    //given
    int accountId = 1;
    Optional<Account> account = accountService.getAccountById(accountId);
    String accountAsJson = convertToJson(account.get());
    //when
    mockMvc
        .perform(get(URL_TEMPLATE + accountId))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(accountAsJson));
  }

  @Test
  @DisplayName("Should delete single account with given id")
  @WithMockUser
  void shouldDeleteAccountWithGivenId() throws Exception {
    //given
    int accountId = 1;
    Optional<Account> account = accountService.getAccountById(accountId);
    //when
    mockMvc
        .perform(delete(URL_TEMPLATE + accountId))
        .andDo(print())
        //then
        .andExpect(status().isNoContent());

    assertFalse(accountService.getAccountById(1).isPresent());
  }

  @Test
  @DisplayName("Should add new account")
  @WithMockUser
  void shouldAddNewAccount() throws Exception {
    //given
    Account account = new Account("john_doe", "johnspassword", "john@doe.com", null);
    String accountAsJson = convertToJson(account);
    //when
    mockMvc
        .perform(post(URL_TEMPLATE)
            .contentType(CONTENT_TYPE_JSON)
            .content(accountAsJson))
        .andDo(print())
        //then
        .andExpect(status().isCreated())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(jsonPath("$.userName").value("john_doe"))
        .andExpect(jsonPath("$.email").value("john@doe.com"));
  }

  @Test
  @DisplayName("Should update existing account")
  @WithMockUser
  void shouldUpdateExistingAccount() throws Exception {
    //given
    Account account = new Account("John Doe", "password", "john@doe.com", null);
    Account savedAccount = accountService.addAccount(account);
    Account updatedAccount = new Account(savedAccount.getId(), "John P. Doe", "updatedPassword", "john@doe2.com", null);
    String updatedAccountAsJson = convertToJson(updatedAccount);

    //when
    mockMvc
        .perform(put(URL_TEMPLATE + savedAccount.getId())
            .contentType(CONTENT_TYPE_JSON)
            .content(updatedAccountAsJson))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(jsonPath("$.userName").value("John P. Doe"))
        .andExpect(jsonPath("$.password").value("updatedPassword"))
        .andExpect(jsonPath("$.email").value("john@doe2.com"));
  }

  @Test
  @DisplayName("Should return Not Found status code when non-existent id is provided on account retrieval")
  @WithMockUser
  void shouldReturnNotFoundWhenBadIdProvidedOnAccountRetrieval() throws Exception {
    //given
    int accountId = 100;

    //when
    mockMvc
        .perform(get(URL_TEMPLATE + accountId))
        .andDo(print())
        //then
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Should return Not Found status code when non-existent id is provided on account update")
  @WithMockUser
  void shouldReturnNotFoundWhenBadIdProvidedOnAccountUpdate() throws Exception {
    //given
    int accountId = 100;
    Account account = new Account("John Doe", "password", "john@doe.com", null);
    Account savedAccount = accountService.addAccount(account);
    Account updatedAccount = new Account(savedAccount.getId(), "John P. Doe", "updatedPassword", "john@doe2.com", null);
    String updatedAccountAsJson = convertToJson(updatedAccount);

    //when
    mockMvc
        .perform(put(URL_TEMPLATE + accountId)
            .contentType(CONTENT_TYPE_JSON)
            .content(updatedAccountAsJson))
        .andDo(print())
        //then
        .andExpect(status().isNotFound());
  }
}
