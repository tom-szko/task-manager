package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.repository.TaskRepository;
import com.szkopinski.todoo.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Optional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tasks")
@CrossOrigin
@Api("/api/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @ApiOperation(value = "Finds all tasks", notes = "Retrieving the collection of tasks", response = Task[].class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Task[].class)
    })
    public ResponseEntity<Iterable<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @GetMapping("/{taskId}")
    @ApiOperation(value = "Finds single task", notes = "Retrieves a single task", response = Task.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Task.class),
            @ApiResponse(code = 404, message = "Not found")
    })
    public ResponseEntity getTask(@ApiParam(value = "Id of invoice to retrieve", required = true) @PathVariable("taskId") String taskId) {
        Optional<Task> task = taskService.findTask(Integer.valueOf(taskId));
        if (task.isPresent()) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ApiOperation(value = "Adds single task", notes = "Adds a single task")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Task.class),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity addTask(@RequestBody @Valid Task task) {
        try {
            return ResponseEntity.ok(taskService.addTask(task));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{taskId}")
    @ApiOperation(value = "Removes single task", notes = "Removes a single task")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Success"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity deleteTask(@PathVariable("taskId") String taskId) {
        try {
            taskService.deleteTask(Integer.valueOf(taskId));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{taskId}")
    @ApiOperation(value = "Updates single task", notes = "Updates a single task")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Not found")
    })
    public ResponseEntity updateTask(@PathVariable("taskId") String taskId, @Valid @RequestBody Task updatedTask) {
        try {
            return taskService.findTask(Integer.valueOf(taskId))
                    .map(task -> {
                        task.setDeadline(updatedTask.getDeadline());
                        task.setCreationDate(updatedTask.getCreationDate());
                        task.setCompleted(updatedTask.isCompleted());
                        task.setContents(updatedTask.getContents());
                        task.setChecklist(updatedTask.getChecklist());
                        return ResponseEntity.ok(task);
                    }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
