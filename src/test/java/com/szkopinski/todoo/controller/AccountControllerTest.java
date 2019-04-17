package com.szkopinski.todoo.controller;

import static com.szkopinski.todoo.helpers.TestHelpers.convertToJson;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountControllerTest {

  private static final String URL_TEMPLATE = "/api/accounts/";
  private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AccountRepository accountService;

  @Test
  @DisplayName("Should retrieve all accounts present in the database")
  @WithMockUser
  void shouldReturnAllAccounts() throws Exception {
    //given
    Iterable<Account> accounts = accountService.findAll();
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
    Optional<Account> account = accountService.findById(accountId);
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
    Optional<Account> account = accountService.findById(accountId);
    String accountAsJson = convertToJson(account.get());
    //when
    mockMvc
        .perform(delete(URL_TEMPLATE + accountId))
        .andDo(print())
        //then
        .andExpect(status().isNoContent());

    assertFalse(accountService.existsById(1));
  }

  @Test
  @DisplayName("Should add new account")
  @WithMockUser
  void shouldAddNewAccount() throws Exception {
    //given
    Account account = new Account("john_doe", "johnspassword", "john@doe.com");
    String accountAsJson = convertToJson(account);
    //when
    mockMvc
        .perform(post(URL_TEMPLATE)
            .contentType(CONTENT_TYPE_JSON)
            .content(accountAsJson))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(jsonPath("$.userName").value("john_doe"))
        .andExpect(jsonPath("$.email").value("john@doe.com"));
  }
}
