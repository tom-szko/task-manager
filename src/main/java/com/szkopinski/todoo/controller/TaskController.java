package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.repository.TaskRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

  private TaskRepository taskRepository;

  @Autowired
  TaskController(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @GetMapping
  public ResponseEntity<Iterable<Task>> getAllTasks() {
    return ResponseEntity.ok(taskRepository.findAll());
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<Task> getTask(@PathVariable("taskId") String taskId) {
    Optional<Task> task = taskRepository.findById(Integer.valueOf(taskId));
    if (task.isPresent()) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity addTask(@RequestBody Task task) {
    try {
      return ResponseEntity.ok(taskRepository.save(task));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity deleteTask(@PathVariable("taskId") String taskId) {
    try {
      taskRepository.deleteById(Integer.valueOf(taskId));
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
