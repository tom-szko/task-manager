package com.szkopinski.todoo.service;

import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.repository.TaskRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private TaskRepository taskRepository;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Transactional
  public Task addTask(Task task) {
    return taskRepository.save(task);
  }

  @Transactional
  public void deleteTask(int taskId) {
    taskRepository.deleteById(taskId);
  }

  public Optional<Task> findTask(int taskId) {
    return taskRepository.findById(taskId);
  }

  public Iterable<Task> findAllTasks() {
    return taskRepository.findAll();
  }

  @Transactional
  public Task updateTask(int taskId, Task updatedTask) {
    return taskRepository.findById(taskId)
        .map(task -> {
          task.setContents(updatedTask.getContents());
          task.setCompleted(updatedTask.isCompleted());
          task.setChecklist(updatedTask.getChecklist());
          task.setDeadline(updatedTask.getDeadline());
          return task;
        }).orElse(null);
  }
}
