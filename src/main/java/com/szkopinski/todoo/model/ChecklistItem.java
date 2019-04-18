package com.szkopinski.todoo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
public class ChecklistItem {
  @Id
  @JsonProperty("id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @NotNull(message = "description is a required field")
  @Size(min = 1, max = 1024, message = "description cannot be shorter than 1 and longer than 1024 characters")
  private String description;

  private boolean completed;
}
