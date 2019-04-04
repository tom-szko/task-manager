package com.szkopinski.todoo.model;

import lombok.Data;

@Data
public class Task {

  private int id;
  private String contents;
  private boolean isDone;

}
