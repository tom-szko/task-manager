package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.repository.AccountRepository;
import com.szkopinski.todoo.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.szkopinski.todoo.helpers.TestHelpers.convertToJson;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

  private static final String URL_TEMPLATE = "/api/tasks/";
  private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @MockBean
  private AccountRepository accountRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Test
  @WithMockUser
  @DisplayName("Should return all tasks")
  void shouldReturnAllTasks() throws Exception {

    //given
    Task task1 = new Task("Some text", false, new ArrayList<>(), LocalDate.of(2019, 4, 3), LocalDate.of(2019, 5, 16), "user");
    Task task2 = new Task("Some text2", false, new ArrayList<>(), LocalDate.of(2019, 4, 4), LocalDate.of(2019, 5, 20), "user");
    Task task3 = new Task("Some text3", false, new ArrayList<>(), LocalDate.of(2019, 4, 5), LocalDate.of(2019, 5, 22), "user");
    Iterable<Task> tasks = List.of(task1, task2, task3);
    String tasksAsJson = convertToJson(tasks);
    when(taskService.findAllTasks()).thenReturn(tasks);

    //when
    mockMvc
        .perform(get(URL_TEMPLATE)
            .accept(CONTENT_TYPE_JSON))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(tasksAsJson));

    verify(taskService).findAllTasks();
  }

  @Test
  @WithMockUser
  @DisplayName("Should return task with given Id number")
  void shouldReturnTaskWithGivenId() throws Exception {
    //given
    int taskId = 1;
    Task task = new Task("Some text", false, new ArrayList<>(), LocalDate.of(2019, 4, 3), LocalDate.of(2019, 5, 16), "user");
    String taskAsJson = convertToJson(task);
    when(taskService.findTask(taskId)).thenReturn(Optional.of(task));
    //when
    mockMvc
        .perform(get(URL_TEMPLATE + taskId)
            .accept(CONTENT_TYPE_JSON))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(taskAsJson));

    verify(taskService).findTask(taskId);
  }

  @Test
  @WithMockUser
  @DisplayName("Should add new task")
  void shouldAddNewTask() throws Exception {
    //given
    Task task = new Task("Read Effective Java book", false, new ArrayList<>(), LocalDate.of(2019, 5, 14), LocalDate.of(2019, 8, 14), "user");
    String taskAsJson = convertToJson(task);
    when(taskService.addTask(task)).thenReturn(task);
    //when
    mockMvc
        .perform(post(URL_TEMPLATE)
            .contentType(CONTENT_TYPE_JSON)
            .content(taskAsJson))
        .andDo(print())
        //then
        .andExpect(status().isCreated())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().json(taskAsJson));

    verify(taskService).addTask(task);
  }

  @Test
  @WithMockUser
  @DisplayName("Should delete task with given id")
  void shouldDeleteTaskWithGivenId() throws Exception {
    //given
    int taskId = 1;
    doNothing().when(taskService).deleteTask(taskId);

    //when
    mockMvc
        .perform(delete(URL_TEMPLATE + taskId)
            .accept(CONTENT_TYPE_JSON))
        .andDo(print())
        //then
        .andExpect(status().isNoContent());

    verify(taskService).deleteTask(taskId);
  }

  @Test
  @WithMockUser
  @DisplayName("Should update task with given id number")
  void shouldUpdateTaskWithGivenIdNumber() throws Exception {
    //given
    int taskId = 1;
    Task updatedTask = new Task("Updated Text", false, new ArrayList<>(), LocalDate.of(2019, 4, 13), LocalDate.of(2019, 5, 26), "user");
    String updatedTaskAsJson = convertToJson(updatedTask);
    when(taskService.updateTask(taskId, updatedTask)).thenReturn(updatedTask);

    //when
    mockMvc
        .perform(put(URL_TEMPLATE + taskId)
            .contentType(CONTENT_TYPE_JSON)
            .content(updatedTaskAsJson))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(content().string(updatedTaskAsJson));

    verify(taskService).updateTask(taskId, updatedTask);
  }
}
