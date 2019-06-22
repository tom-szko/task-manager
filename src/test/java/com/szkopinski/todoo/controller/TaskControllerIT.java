package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.ChecklistItem;
import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.szkopinski.todoo.helpers.TestHelpers.convertToJson;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerIT {

  private static final String URL_TEMPLATE = "/api/tasks/";
  private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_UTF8;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TaskService taskService;

  @Test
  @DisplayName("Should retrieve all tasks present in the database")
  @WithMockUser
  void shouldReturnAllTasks() throws Exception {
    //when
    mockMvc
        .perform(get(URL_TEMPLATE))
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
    int taskId = 1;
    Task task = new Task(taskId, "Read Effective Java book", false, new ArrayList<>(), LocalDate.now(), LocalDate.of(2019, 8, 14), "user");
    taskService.addTask(task);
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
    int taskId = 1;
    Task task = new Task(taskId, "Read Effective Java book", false, new ArrayList<>(), LocalDate.now(), LocalDate.of(2019, 8, 14), "user");
    String taskAsJson = convertToJson(task);
    //when
    mockMvc
        .perform(post(URL_TEMPLATE)
            .contentType(CONTENT_TYPE_JSON)
            .content(taskAsJson))
        .andDo(print())
        //then
        .andExpect(status().isCreated())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(jsonPath("$.contents").value(task.getContents()))
        .andExpect(jsonPath("$.completed").value(task.isCompleted()));
  }

  @Test
  @DisplayName("Should remove single task from database")
  @WithMockUser
  void shouldRemoveSingleTask() throws Exception {
    //given
    int taskId = 1;
    Task task = new Task(taskId, "Read Effective Java book", false, new ArrayList<>(), LocalDate.now(), LocalDate.of(2019, 8, 14), "user");
    taskService.addTask(task);
    //when
    mockMvc
        .perform(delete(URL_TEMPLATE + taskId))
        .andDo(print())
        //then
        .andExpect(status().isNoContent());

    assertFalse(taskService.findTask(taskId).isPresent());
  }

  @Test
  @DisplayName("Should update single task")
  @WithMockUser
  void shouldUpdateSingleTask() throws Exception {
    //given
    Task task = new Task("Some title", false, new ArrayList<>(), LocalDate.of(2019, 4, 12), LocalDate.of(2019, 5, 5), "user");
    Task addedTask = taskService.addTask(task);
    List<ChecklistItem> updatedChecklistItems = new ArrayList<>();
    updatedChecklistItems.add(new ChecklistItem("Buy milk", false));
    updatedChecklistItems.add(new ChecklistItem("Wash dishes", true));
    Task updatedTask = new Task(addedTask.getId(), "Updated title", false, updatedChecklistItems, task.getCreationDate(), LocalDate.of(2019, 6,
            20), "user");
    String updatedTaskAsJson = convertToJson(updatedTask);

    //when
    mockMvc
        .perform(put(URL_TEMPLATE + addedTask.getId())
            .contentType(CONTENT_TYPE_JSON)
            .content(updatedTaskAsJson))
        .andDo(print())
        //then
        .andExpect(status().isOk())
        .andExpect(content().contentType(CONTENT_TYPE_JSON))
        .andExpect(jsonPath("$.id").value(updatedTask.getId()))
        .andExpect(jsonPath("$.contents").value(updatedTask.getContents()))
        .andExpect(jsonPath("$.completed").value(updatedTask.isCompleted()))
        .andExpect(jsonPath("$.checklist[0].description").value(updatedTask.getChecklist().get(0).getDescription()))
        .andExpect(jsonPath("$.checklist[0].completed").value(updatedTask.getChecklist().get(0).isCompleted()))
        .andExpect(jsonPath("$.checklist[1].description").value(updatedTask.getChecklist().get(1).getDescription()))
        .andExpect(jsonPath("$.checklist[1].completed").value(updatedTask.getChecklist().get(1).isCompleted()))
        .andExpect(jsonPath("$.creationDate").value(updatedTask.getCreationDate().toString()))
        .andExpect(jsonPath("$.deadline").value(updatedTask.getDeadline().toString()))
            .andExpect(jsonPath("$.userName.name").value(updatedTask.getUserName()));
  }
}
