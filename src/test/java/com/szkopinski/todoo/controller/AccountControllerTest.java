package com.szkopinski.todoo.controller;

import static com.szkopinski.todoo.helpers.TestHelpers.convertToJson;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sun.tools.javac.util.List;
import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import com.szkopinski.todoo.service.AccountService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

  private static final String URL_TEMPLATE = "/api/accounts/";
  private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccountService accountService;

  @MockBean
  private AccountRepository accountRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Test
  @WithMockUser
  @DisplayName("Should return all accounts")
  void shouldReturnAllAccounts() throws Exception {

    //given
    Account account1 = new Account("user1", "password1", "email1@email.com", null);
    Account account2 = new Account("user2", "password2", "email2@email.com", null);
    Account account3 = new Account("user3", "password3", "email3@email.com", null);
    Iterable<Account> accounts = List.of(account1, account2, account3);
    String accountsAsJson = convertToJson(accounts);
    when(accountService.getAllAccounts()).thenReturn(accounts);

    //when
    mockMvc
        .perform(get(URL_TEMPLATE)
            .accept(CONTENT_TYPE_JSON))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(accountsAsJson));

    verify(accountService).getAllAccounts();
  }

  @Test
  @WithMockUser
  @DisplayName("Should return account with given id number")
  void shouldReturnAccountWithGivenId() throws Exception {
    //given
    int accountId = 1;
    Account account = new Account("user", "password", "email1@email.com", null);
    String accountAsJson = convertToJson(account);
    when(accountService.getAccountById(accountId)).thenReturn(Optional.of(account));

    //when
    mockMvc
        .perform(get(URL_TEMPLATE + accountId)
            .accept(CONTENT_TYPE_JSON))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(accountAsJson));

    verify(accountService).getAccountById(accountId);
  }

  @Test
  @WithMockUser
  @DisplayName("Should add new user account")
  void shouldAddNewUserAccount() throws Exception {
    //given
    Account account = new Account("user", "password", "user@email.com", null);
    String accountAsJson = convertToJson(account);
    when(accountService.addAccount(account)).thenReturn(account);
    //when
    mockMvc
        .perform(post(URL_TEMPLATE)
            .contentType(CONTENT_TYPE_JSON)
            .content(accountAsJson))
        .andDo(print())
        //then
        .andExpect(status().isCreated())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().string(accountAsJson));

    verify(accountService).addAccount(account);
  }

  @Test
  @WithMockUser
  @DisplayName("Should delete user account with given id")
  void shouldDeleteAccountWithGivenId() throws Exception {
    //given
    int accountId = 1;
    doNothing().when(accountService).deleteAccount(accountId);

    //when
    mockMvc
        .perform(delete(URL_TEMPLATE + accountId)
            .accept(CONTENT_TYPE_JSON))
        .andDo(print())
        //then
        .andExpect(status().isNoContent());

    verify(accountService).deleteAccount(accountId);
  }

  @Test
  @WithMockUser
  @DisplayName("Should update user account with given id")
  void shouldUpdateAccountWithGivenId() throws Exception {
    //given
    int accountId = 1;
    Account updatedAccount = new Account("updated_user", "updated_password", "updated@email.com", null);
    String updatedAccountAsJson = convertToJson(updatedAccount);
    when(accountService.updateAccount(accountId, updatedAccount)).thenReturn(updatedAccount);

    //when
    mockMvc
        .perform(put(URL_TEMPLATE + accountId)
            .contentType(CONTENT_TYPE_JSON)
            .content(updatedAccountAsJson))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().string(updatedAccountAsJson));

    verify(accountService).updateAccount(accountId, updatedAccount);
  }

  @Test
  @WithMockUser
  @DisplayName("Should return Not Found status when account id cannot be found")
  void shouldReturnNotFoundWhenAccountIsNotFound() throws Exception {
    //given
    int accountId = 1;
    Account updatedAccount = new Account("updated_user", "updated_password", "updated@email.com", null);
    String updatedAccountAsJson = convertToJson(updatedAccount);
    when(accountService.updateAccount(accountId, updatedAccount)).thenReturn(null);

    //when
    mockMvc
        .perform(put(URL_TEMPLATE + accountId)
            .content(updatedAccountAsJson)
            .contentType(CONTENT_TYPE_JSON))
        .andDo(print())

        //then
        .andExpect(status().isNotFound());

    verify(accountService).updateAccount(accountId, updatedAccount);
  }

  @Test
  @WithMockUser
  @DisplayName("Should return Internal Server Error status when exception is throw while updating account")
  void shouldReturnInternalServerErrorWhenExceptionIsThrowOnAccountUpdate() throws Exception {
    //given
    int accountId = 1;
    Account updatedAccount = new Account("updated_user", "updated_password", "updated@email.com", null);
    String updatedAccountAsJson = convertToJson(updatedAccount);
    doThrow(NullPointerException.class).when(accountService).updateAccount(accountId, updatedAccount);

    //when
    mockMvc
        .perform(put(URL_TEMPLATE + accountId)
            .content(updatedAccountAsJson)
            .contentType(CONTENT_TYPE_JSON))
        .andDo(print())

        //then
        .andExpect(status().isInternalServerError());

    verify(accountService).updateAccount(accountId, updatedAccount);
  }

  @Test
  @WithMockUser
  @DisplayName("Should return Bad Request status when invalid id is passed as parameter for deleteAccount")
  void shouldReturnBadRequestOnDeleteAccount() throws Exception {
    //given
    int accountId = 0;
    doNothing().when(accountService).deleteAccount(accountId);

    //when
    mockMvc
        .perform(delete(URL_TEMPLATE + accountId)
            .accept(CONTENT_TYPE_JSON))
        .andDo(print())
        //then
        .andExpect(status().isBadRequest());
  }
}
