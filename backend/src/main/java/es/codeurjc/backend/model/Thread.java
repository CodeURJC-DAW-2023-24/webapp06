package es.codeurjc.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Transient;

@Entity
@Table(name = "threads")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt = new Date();

    @Transient
    private int numberPosts;

    // Constructors
    public Thread() {
    }

    public Thread(String name, Forum forum, List<Post> posts, User owner) {
        this.name = name;
        this.forum = forum;
        this.posts = posts;
        this.owner = owner;
        this.numberPosts = posts.size();
    }

    // Getters and Setters
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

    public User getCreator() {
        return owner;
    }

    public void setCreator(User creator) {
        this.owner = creator;
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

    // toString method
    @Override
    public String toString() {
        return "Thread{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", posts=" + posts +
                ", creator=" + owner +
                ", createdAt=" + createdAt +
                '}';
    }
}
