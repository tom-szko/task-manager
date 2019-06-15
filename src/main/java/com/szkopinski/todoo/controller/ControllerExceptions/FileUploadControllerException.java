package com.szkopinski.todoo.controller.ControllerExceptions;

public class FileUploadControllerException extends Exception {

  public FileUploadControllerException(String message) {
    super(message);
  }

  public FileUploadControllerException(String message, Throwable cause) {
    super(message, cause);
  }
}
