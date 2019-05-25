package com.szkopinski.todoo.service;

import com.szkopinski.todoo.model.Task;
import com.szkopinski.todoo.repository.TaskRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  private TaskRepository taskRepository;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Transactional
  public Task addTask(@NonNull Task task) {
    task.setCreationDate(LocalDate.now());
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
    Sort sortByCreationDate = new Sort(Sort.Direction.DESC, "creationDate");
    return taskRepository.findAll(sortByCreationDate);
  }

  public List<Task> findAllTasksByUserName(String userName) {
    List<Task> tasks = new ArrayList<>();
    findAllTasks().forEach(tasks::add);
    return tasks.stream()
        .filter(task -> task.getUserName().getName().equals(userName))
        .collect(Collectors.toList());
  }

  @Transactional
  public Task updateTask(int taskId, @NonNull Task task) {
    return taskRepository.findById(taskId)
        .map(taskData -> {
          taskData.setContents(task.getContents());
          taskData.setCompleted(task.isCompleted());
          taskData.setChecklist(task.getChecklist());
          taskData.setDeadline(task.getDeadline());
          Task updatedTask = taskRepository.save(taskData);
          return updatedTask;
        }).orElse(null);
  }
}
