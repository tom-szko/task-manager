package com.szkopinski.todoo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.repository.TaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  private TaskRepository repository;

  private TaskService taskService;

  @BeforeEach
  void setUp() {
    taskService = new TaskService(repository);
  }

  @Test
  @DisplayName("Should return saved task when addTask gets invoked")
  void shouldReturnAddedTask() {
    //given
    Task task = new Task(1, "Some title", false, new ArrayList<>(), LocalDate.of(2019, 4, 12), LocalDate.of(2019, 05, 05));
    when(repository.save(task)).thenReturn(task);

    //when
    Task addedTask = taskService.addTask(task);

    //then
    assertEquals(addedTask, task);
    verify(repository, times(1)).save(task);
  }

  @Test
  @DisplayName("Should delete of existing task with given id number")
  void shouldDeleteExistingTask() {
    //given
    int taskId = 1;
    doNothing().when(repository).deleteById(taskId);

    //when
    taskService.deleteTask(taskId);

    //then
    verify(repository).deleteById(taskId);
  }

  @Test
  @DisplayName("Should find and return existing task with given id number")
  void shouldReturnExistingTask() {
    //given
    int taskId = 1;
    doNothing().when(repository).deleteById(taskId);

    //when
    taskService.deleteTask(taskId);

    //then
    verify(repository).deleteById(taskId);
  }

  @Test
  @DisplayName("Should find and return all existing tasks")
  void shouldReturnAllTasks() {
    //given
    List<Task> tasks = new ArrayList<>();
    Task task1 = new Task(1, "Some title", false, new ArrayList<>(), LocalDate.of(2019, 4, 12), LocalDate.of(2019, 5, 5));
    Task task2 = new Task(2, "Some title 2", false, new ArrayList<>(), LocalDate.of(2019, 4, 12), LocalDate.of(2019, 5, 6));
    tasks.add(task1);
    tasks.add(task2);

    when(repository.findAll()).thenReturn(tasks);

    //when
    Iterable<Task> result = taskService.findAllTasks();

    //then
    assertEquals(tasks, result);
    verify(repository).findAll();
  }


  @Test
  @DisplayName("Should update existing task with given id number")
  void shouldUpdateExistingTask() {
    //given
    int taskId = 1;
    Task task = new Task(taskId, "Some title", false, new ArrayList<>(), LocalDate.of(2019, 4, 12), LocalDate.of(2019, 5, 5));
    Task updatedTask = new Task(taskId, "Updated title", false, new ArrayList<>(), LocalDate.of(2019, 4, 12), LocalDate.of(2019, 5, 20));
    when(repository.findById(taskId)).thenReturn(Optional.of(task));

    //when
    Task result = taskService.updateTask(taskId, updatedTask);

    //then
    assertEquals(updatedTask, result);
    verify(repository).findById(taskId);
  }
}
