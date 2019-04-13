package com.szkopinski.todoo;

import com.szkopinski.todoo.model.User;
import com.szkopinski.todoo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodooApplication implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public static void main(String[] args) {
    SpringApplication.run(TodooApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    User user1 = new User("admin", passwordEncoder.encode("password"), "admin@szkopinski.com");
    userRepository.save(user1);
    System.out.println(user1.toString());
    User user2 = new User("user", passwordEncoder.encode("1234"), "admin@szkopinski.com");
    userRepository.save(user2);
    System.out.println(user2.toString());
  }
}
