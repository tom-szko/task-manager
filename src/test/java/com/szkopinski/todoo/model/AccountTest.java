package com.szkopinski.todoo.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountTest {

  @Test
  void checkFullInitialization() {
    //given
    String userName = "John Doe";
    String password = "password";
    String email = "john@doe.com";

    //when
    Account account = new Account(userName, password, email);

    //then
    assertEquals(userName, account.getUserName());
    assertEquals(password, account.getPassword());
    assertEquals(email, account.getEmail());
  }
}
