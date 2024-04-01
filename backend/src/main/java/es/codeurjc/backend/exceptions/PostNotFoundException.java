package es.codeurjc.backend.exceptions;

public class PostNotFoundException extends RuntimeException {
  public PostNotFoundException(String message) {
    super(message);
  }
}