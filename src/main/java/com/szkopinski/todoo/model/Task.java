package com.szkopinski.todoo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Task model")
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
public class Task {

  @Id
  @JsonProperty("id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  @ApiModelProperty(value = "Id of given task", dataType = "Integer")
  private int id;

  @NotNull(message = "contents is a required field")
  @Size(min = 1, max = 1024, message = "contents cannot be shorter than 1 and longer than 1024 characters")
  @ApiModelProperty(value = "Text contents of given task", required = true, dataType = "String", example = "Take dog for a walk.")
  private String contents;

  @ApiModelProperty(name = "completed", dataType = "boolean")
  private boolean completed;

  //TODO - add swagger and hibernate annotations
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChecklistItem> checklist;

}
