package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/")
@Validated
@CrossOrigin
@Api("/api/")
public class TaskController {

  private TaskService taskService;

  @Autowired
  TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("tasks")
  @ApiOperation(value = "Finds all tasks", notes = "Retrieving the collection of tasks", response = Task[].class)
  @ApiResponses( {
      @ApiResponse(code = 200, message = "Success", response = Task[].class)
  })
  public ResponseEntity<Iterable<Task>> getAllTasks() {
    return ResponseEntity.ok(taskService.findAllTasks());
  }

  @GetMapping("/{userName}/tasks")
  @ApiOperation(value = "Finds all tasks for logged in user", notes = "Retrieving the collection of tasks for current user name", response =
      Task[].class)
  @ApiResponses( {
      @ApiResponse(code = 200, message = "Success", response = Task[].class)
  })
  public ResponseEntity<Iterable<Task>> getAllTasksForCurrentUser(@ApiIgnore Authentication authentication,
      @PathVariable("userName") String userName) {
    if (authentication.getName().equals(userName)) {
      return ResponseEntity.ok(taskService.findAllTasksByUserName(userName));
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @GetMapping("tasks/{taskId}")
  @ApiOperation(value = "Finds single task", notes = "Retrieves a single task", response = Task.class)
  @ApiResponses( {
      @ApiResponse(code = 200, message = "Success", response = Task.class),
      @ApiResponse(code = 404, message = "Not found")
  })
  public ResponseEntity getTask(@ApiParam(value = "Id of invoice to retrieve", required = true) @Min(1) @PathVariable("taskId") int taskId) {
    Optional<Task> task = taskService.findTask(taskId);
    if (task.isPresent()) {
      return ResponseEntity.ok(task);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("tasks")
  @ApiOperation(value = "Adds single task", notes = "Adds a single task")
  @ApiResponses( {
      @ApiResponse(code = 200, message = "Success", response = Task.class),
      @ApiResponse(code = 500, message = "Internal Server Error")
  })
  public ResponseEntity addTask(@Valid @RequestBody Task task) {
    try {
      return ResponseEntity.status(HttpStatus.CREATED).body(taskService.addTask(task));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @DeleteMapping("tasks/{taskId}")
  @ApiOperation(value = "Removes single task", notes = "Removes a single task")
  @ApiResponses( {
      @ApiResponse(code = 204, message = "Success"),
      @ApiResponse(code = 500, message = "Internal Server Error")
  })
  public ResponseEntity deleteTask(@Min(1) @PathVariable("taskId") int taskId) {
    try {
      taskService.deleteTask(taskId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PutMapping("tasks/{taskId}")
  @ApiOperation(value = "Updates single task", notes = "Updates a single task")
  @ApiResponses( {
      @ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 404, message = "Not found")
  })
  public ResponseEntity updateTask(@Min(1) @PathVariable("taskId") int taskId, @Valid @RequestBody Task updatedTask) {
    try {
      Task newTask = taskService.updateTask(taskId, updatedTask);
      if (newTask == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(newTask);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
