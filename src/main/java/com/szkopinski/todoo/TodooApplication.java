package com.szkopinski.todoo;

import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.model.ChecklistItem;
import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.model.UserName;
import com.szkopinski.todoo.repository.AccountRepository;
import com.szkopinski.todoo.repository.TaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodooApplication implements CommandLineRunner {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public static void main(String[] args) {
    SpringApplication.run(TodooApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Account account1 = new Account("admin", passwordEncoder.encode("password"), "admin@szkopinski.com", null);
    accountRepository.save(account1);
    System.out.println(account1.toString());
    Account account2 = new Account("tyler", passwordEncoder.encode("password"), "tyler@email.com", null);
    accountRepository.save(account2);
    System.out.println(account2.toString());

    List<ChecklistItem> sampleChecklist = new ArrayList<>();
    sampleChecklist.add(new ChecklistItem("Find your dog", false));
    sampleChecklist.add(new ChecklistItem("Put leash on the dog", false));
    sampleChecklist.add(new ChecklistItem("Go out", false));
    Task sampleTask = new Task(
        "Take dog for a walk",
        false,
        sampleChecklist,
        LocalDate.of(2019, 4, 14),
        LocalDate.of(2019, 5, 24),
        new UserName("admin"));
    taskRepository.save(sampleTask);

    List<ChecklistItem> sampleChecklist2 = new ArrayList<>();
    sampleChecklist2.add(new ChecklistItem("Find your dog2", false));
    sampleChecklist2.add(new ChecklistItem("Put leash on the dog2", false));
    sampleChecklist2.add(new ChecklistItem("Go out2", false));
    Task sampleTask2 = new Task(
        "Take dog for a walk2",
        false,
        sampleChecklist2,
        LocalDate.of(2019, 4, 14),
        LocalDate.of(2019, 5, 24),
        new UserName("admin"));
    taskRepository.save(sampleTask2);
  }
}
