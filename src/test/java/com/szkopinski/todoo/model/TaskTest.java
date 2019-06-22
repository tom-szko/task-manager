package com.szkopinski.todoo.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

  @Test
  void checkFullInitialization() {
    //given
    String contents = "Sample task entry";
    boolean completed = false;
    List<ChecklistItem> checklist = new ArrayList<>();
    LocalDate creationDate = LocalDate.of(2019, 4, 19);
    LocalDate deadline = LocalDate.of(2019, 5, 19);
    String userName = "user";

    //when
    Task task = new Task(contents, completed, checklist, creationDate, deadline, userName);

    //then
    assertEquals(contents, task.getContents());
    assertEquals(completed, task.isCompleted());
    assertEquals(checklist, task.getChecklist());
    assertEquals(creationDate, task.getCreationDate());
    assertEquals(deadline, task.getDeadline());
    assertEquals(userName, task.getUserName());
  }

}