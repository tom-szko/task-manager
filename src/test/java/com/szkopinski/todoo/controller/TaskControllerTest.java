package com.szkopinski.todoo.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  @DisplayName("Should retrieve all tasks present in the database")
  @WithMockUser
  void shouldReturnAllTasks() throws Exception {

    mockMvc
        .perform(get("/api/tasks"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("[]")));
  }

  @Test
  @DisplayName("Should retrieve task with given id number")
  @WithMockUser
  void shouldReturnTaskById() throws Exception {
    //given
    int taskId = 3;
    Task task = new Task(taskId, "Read Star Wars book", false);
    taskRepository.save(task);
    //when
    mockMvc
        .perform(get("/api/tasks/" + taskId))
        .andDo(print())
    //then
        .andExpect(status().isOk())
        .andExpect(content().json(convertToJson(task)));
  }

  String convertToJson(Task task) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(task);
  }
}
