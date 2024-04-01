package es.codeurjc.backend.exceptions;

public class ThreadNotFoundException extends RuntimeException {
  public ThreadNotFoundException(String message) {
    super(message);
  }
}