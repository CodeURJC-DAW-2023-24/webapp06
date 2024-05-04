package es.codeurjc.backend.dto;

import java.util.Date;

import es.codeurjc.backend.model.Post;

public class PostDTO {
    private Long id;
    private String text;
    private Long ownerId;
    private String ownerUsername;
    private Date createdAt;
    private Long threadId;
    private int likes;
    private int dislikes;
    private int reports;

    public PostDTO() {
    }

    public PostDTO(Long id, String text, Long ownerId, String ownerUsername, Date createdAt, Long threadId,
            int likes, int dislikes, int reports) {
        this.id = id;
        this.text = text;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt;
        this.threadId = threadId;
        this.likes = likes;
        this.dislikes = dislikes;
        this.reports = reports;
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.text = post.getText();
        this.ownerId = post.getOwner().getId();
        this.ownerUsername = post.getOwner().getUsername();
        this.createdAt = post.getCreatedAt();
        this.threadId = post.getThread().getId();
        this.likes = post.getLikes();
        this.dislikes = post.getDislikes();
        this.reports = post.getReports();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }
}
