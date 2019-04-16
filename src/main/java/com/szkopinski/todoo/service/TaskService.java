package com.szkopinski.todoo.service;

import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.repository.TaskRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private TaskRepository taskRepository;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public Task addTask(Task task) {
    return taskRepository.save(task);
  }

  public void deleteTask(int taskId) {
    taskRepository.deleteById(taskId);
  }

  public Optional<Task> findTask(int taskId) {
    return taskRepository.findById(taskId);
  }

  public Iterable<Task> findAllTasks() {
    return taskRepository.findAll();
  }
}
