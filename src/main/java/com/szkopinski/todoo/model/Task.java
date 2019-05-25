package com.szkopinski.todoo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@ApiModel(value = "Task model")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Id of given task", dataType = "Integer")
  private int id;

  @NotBlank(message = "contents is a required field")
  @Size(min = 1, max = 1024, message = "contents cannot be shorter than 1 and longer than 1024 characters")
  @ApiModelProperty(value = "Text contents of given task", required = true, dataType = "String", example = "Take dog for a walk.")
  private String contents;

  @ApiModelProperty(name = "completed", dataType = "boolean")
  private boolean completed;

  @OneToMany(cascade = CascadeType.ALL)
  private List<ChecklistItem> checklist;

  @NotNull
  @ApiModelProperty(dataType = "java.sql.Date", notes = "Date format should be YYYY-MM-DD", example = "2019-04-22")
  private LocalDate creationDate;

  @NotNull
  @ApiModelProperty(dataType = "java.sql.Date", notes = "Date format should be YYYY-MM-DD", example = "2019-04-22")
  private LocalDate deadline;

  @NotNull
  @ManyToOne(cascade = CascadeType.ALL)
  @ApiModelProperty(name = "account", example = "john_doe123")
  private UserName userName;

  public Task(String contents, boolean completed, List<ChecklistItem> checklist, LocalDate creationDate, LocalDate deadline, UserName userName) {
    this.contents = contents;
    this.completed = completed;
    this.checklist = checklist;
    this.creationDate = creationDate;
    this.deadline = deadline;
    this.userName = userName;
  }
}
