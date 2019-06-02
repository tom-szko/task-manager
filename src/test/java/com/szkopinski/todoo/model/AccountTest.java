package com.szkopinski.todoo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AccountTest {

  @Test
  void checkFullInitialization() {
    //given
    String userName = "John Doe";
    String password = "password";
    String email = "john@doe.com";
    ImageFile avatar = null;

    //when
    Account account = new Account(userName, password, email, avatar);

    //then
    assertEquals(userName, account.getUserName());
    assertEquals(password, account.getPassword());
    assertEquals(email, account.getEmail());
    assertEquals(avatar, account.getAvatar());
  }
}
