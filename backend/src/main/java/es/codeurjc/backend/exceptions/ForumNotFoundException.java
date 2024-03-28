package es.codeurjc.backend.exceptions;

public class ForumNotFoundException extends RuntimeException {
  public ForumNotFoundException(String message) {
    super(message);
  }
}