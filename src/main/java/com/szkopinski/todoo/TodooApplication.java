package com.szkopinski.todoo;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodooApplication implements CommandLineRunner {

  @Autowired
  private AccountRepository accountService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public static void main(String[] args) {
    SpringApplication.run(TodooApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Account account1 = new Account("admin", passwordEncoder.encode("password"), "admin@szkopinski.com");
    accountService.save(account1);
    System.out.println(account1.toString());
    Account account2 = new Account("tyler", passwordEncoder.encode("password"), "tyler@email.com");
    accountService.save(account2);
    System.out.println(account2.toString());
  }
}
