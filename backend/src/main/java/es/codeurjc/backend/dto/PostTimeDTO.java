package es.codeurjc.backend.dto;

import java.time.*;

import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.User;

public class PostTimeDTO {
    private Long id;
    private String username;
    private String elapsedTime;
    private String text;
    private boolean hasImage;
    private boolean isLiked;
    private int likes;
    private boolean isDisliked;
    private int dislikes;
    private boolean isOwner;

    public PostTimeDTO() {
    }

    public PostTimeDTO(Post post, User activeUser, boolean isAdmin) {
        this.id = post.getId();
        this.username = post.getOwner().getUsername();

        Instant postTime = post.getCreatedAt().toInstant();
        Instant currentTime = Instant.now();
        Duration duration = Duration.between(postTime, currentTime);

        long seconds = duration.getSeconds();
        long days = seconds / (60 * 60 * 24);
        long hours = seconds / (60 * 60);
        long minutes = seconds / 60;

        if (days > 0) {
            this.elapsedTime = days + " days ago";
        } else if (hours > 0) {
            this.elapsedTime = hours + " hours ago";
        } else if (minutes > 0) {
            this.elapsedTime = minutes + " minutes ago";
        } else
            this.elapsedTime = seconds + " seconds ago";

        this.text = post.getText();

        this.hasImage = post.getImageFile() != null;

        this.likes = post.getLikes();

        this.dislikes = post.getDislikes();

        if (activeUser != null) {
            this.isLiked = post.getUserLikes().contains(activeUser);
            this.isDisliked = post.getUserDislikes().contains(activeUser);
            this.isOwner = (activeUser.getUsername().equals(this.username)) || isAdmin;
        } else {
            this.isLiked = false;
            this.isDisliked = false;
            this.isOwner = false;
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public String getText() {
        return text;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isDisliked() {
        return isDisliked;
    }

    public int getDislikes() {
        return dislikes;
    }

    public boolean isOwner() {
        return isOwner;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDisliked(boolean disliked) {
        isDisliked = disliked;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
