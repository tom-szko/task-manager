package com.szkopinski.todoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "checklistItems")
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "description is a required field")
    @Size(min = 1, max = 1024, message = "description cannot be shorter than 1 and longer than 1024 characters")
    private String description;

    private boolean completed;

    public ChecklistItem(String description, boolean completed) {
        this.description = description;
        this.completed = completed;
    }
}
