package com.szkopinski.todoo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

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
    @JoinColumn(name = "task_id")
    private List<ChecklistItem> checklist;

    @NotNull
    @ApiModelProperty(dataType = "java.sql.Date", notes = "Date format should be YYYY-MM-DD", example = "2019-04-22")
    private LocalDate creationDate;

    @NotNull
    @ApiModelProperty(dataType = "java.sql.Date", notes = "Date format should be YYYY-MM-DD", example = "2019-04-22")
    private LocalDate deadline;

    @NotNull
    @ApiModelProperty(name = "username", example = "john_doe123")
    private String userName;

    public Task(String contents, boolean completed, List<ChecklistItem> checklist, LocalDate creationDate, LocalDate deadline, String userName) {
        this.contents = contents;
        this.completed = completed;
        this.checklist = checklist;
        this.creationDate = creationDate;
        this.deadline = deadline;
        this.userName = userName;
    }
}
