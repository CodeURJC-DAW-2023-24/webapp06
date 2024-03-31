package es.codeurjc.backend.dto;

import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.User;

public class PostReportDTO {
    private Long id;

    private String text;

    private User owner;

    private Thread thread;

    private int reports;

    public PostReportDTO() {
    };

    public PostReportDTO(Post post) {
        id = post.getId();
        text = post.getText();
        owner = post.getOwner();
        thread = post.getThread();
        reports = post.getReports();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getOwner() {
        return owner;
    }

    public Thread getThread() {
        return thread;
    }

    public int getReports() {
        return reports;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }
}
