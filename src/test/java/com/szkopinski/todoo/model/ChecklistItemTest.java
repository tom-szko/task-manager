package com.szkopinski.todoo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ChecklistItemTest {

  @Test
  void checkFullInitialization() {
    //given
    String description = "Sample description";
    boolean completed = false;

    //when
    ChecklistItem checklistItem = new ChecklistItem(description, completed);

    //then
    assertEquals(description, checklistItem.getDescription());
    assertEquals(completed, checklistItem.isCompleted());
  }
}
