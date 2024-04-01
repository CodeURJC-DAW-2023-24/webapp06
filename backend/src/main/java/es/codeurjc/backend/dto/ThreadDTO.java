package es.codeurjc.backend.dto;

import java.util.Date;
import java.util.List;

import es.codeurjc.backend.model.Forum;
import es.codeurjc.backend.model.Thread;
import es.codeurjc.backend.model.Post;
import es.codeurjc.backend.model.User;

public class ThreadDTO {
    private Long id;
    private String name;
    private Forum forum;
    private List<Post> posts;
    private User owner;
    private Date createdAt = new Date();
    private int numberPosts;

    public ThreadDTO() {
    }

    public ThreadDTO(Long id, String name, Forum forum, List<Post> posts, User owner, Date createdAt) {
        this.id = id;
        this.name = name;
        this.forum = forum;
        this.posts = posts;
        this.owner = owner;
        this.createdAt = createdAt;
        this.numberPosts = posts.size();
    }

    public ThreadDTO(Thread thread) {
        this.id = thread.getId();
        this.name = thread.getName();
        this.forum = thread.getForum();
        this.posts = thread.getPosts();
        this.owner = thread.getOwner();
        this.createdAt = thread.getCreatedAt();
        this.numberPosts = thread.getNumberPosts();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        this.numberPosts = posts.size();
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getNumberPosts() {
        return numberPosts;
    }

    public void setNumberPosts() {
        this.numberPosts = this.posts.size();
    }

}