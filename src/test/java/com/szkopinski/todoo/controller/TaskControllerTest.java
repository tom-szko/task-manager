package com.szkopinski.todoo.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerTest {

  private static final String URL_TEMPLATE = "/api/tasks/";
  private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  @DisplayName("Should retrieve all tasks present in the database")
  @WithMockUser
  void shouldReturnAllTasks() throws Exception {
    //when
    mockMvc
        .perform(get(String.format(URL_TEMPLATE)))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json("[]"));
  }

  @Test
  @DisplayName("Should retrieve task with given id number")
  @WithMockUser
  void shouldReturnTaskById() throws Exception {
    //given
    int taskId = 3;
    Task task = new Task(taskId, "Read Effective Java book", false);
    taskRepository.save(task);
    //when
    mockMvc
        .perform(get(URL_TEMPLATE + taskId))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(convertToJson(task)));
  }

  @Test
  @DisplayName("Should add single task to database")
  @WithMockUser
  void shouldAddNewTask() throws Exception {
    //given
    int taskId = 6;
    Task task = new Task(taskId, "Read Effective Java book", false);
    String taskAsJson = convertToJson(task);
    //when
    mockMvc
        .perform(post(URL_TEMPLATE)
            .contentType(CONTENT_TYPE_JSON)
            .content(taskAsJson))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(taskAsJson));
  }

  @Test
  @DisplayName("Should remove single task from database")
  @WithMockUser
  void shouldRemoveSingleTask() throws Exception {
    //given
    int taskId = 3;
    Task task = new Task(taskId, "Read Effective Java book", false);
    taskRepository.save(task);
    //when
    mockMvc
        .perform(delete(URL_TEMPLATE + 3))
        .andDo(print())
        //then
        .andExpect(status().isNoContent());

    assertFalse(taskRepository.findById(3).isPresent());
  }

  String convertToJson(Task task) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(task);
  }
}