package es.codeurjc.backend.dto;

import java.util.Date;

import es.codeurjc.backend.model.Post;

public class PostDTO {
    private Long id;
    private String text;
    //private Blob imageFile;
    private String ownerUsername;
    private Date createdAt;
    private String threadName;
    private int likes;
    private int dislikes;
    private int reports;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.text = post.getText();
        this.ownerUsername = post.getOwner().getUsername();
        this.createdAt = post.getCreatedAt();
        this.threadName = post.getThread().getName();
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
    /*
    public Blob getImageFile() {
        return imageFile;
    }

    public void setImage(Blob imageFile) {
        this.imageFile = imageFile;
    }
    */
    public String getOwnerUsrname() {
        return ownerUsername;
    }

    public void setOwner(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThread(String threadName) {
        this.threadName = threadName;
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
