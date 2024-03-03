package es.codeurjc.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.sql.Blob;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String text;
    
    @Lob
    @JsonIgnore
    private Blob imageFile;
    
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt = new Date();

    @ManyToOne
    @JsonIgnore
    private Thread thread;
    
    @ManyToMany
    private List<User> userLikes;

    @ManyToMany
    private List<User> userDislikes;

    @Column(nullable = false)
    private int reports;

    // Constructors, getters, and setters
    public Post() {
        // Default constructor
    }

    public Post(String text, Blob imageFile, User owner, List<User> userLikes, List<User> userDislikes, int reports) throws Exception {
        super();
        this.text = text;
        this.imageFile = imageFile;
        this.owner = owner;
        this.userLikes = userLikes;
        this.userDislikes = userDislikes;
        this.reports = reports;
    }

    // Getters and setters
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

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImage(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User creator) {
        this.owner = creator;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<User> getUserLikes() {
        return userLikes;
    }

    public int getLikes() {
        return userLikes.size();
    }

    public void setUserLikes(List<User> userLikes) {
        this.userLikes = userLikes;
    }

    public List<User> getUserDislikes() {
        return userDislikes;
    }

    public int getDislikes() {
        return userDislikes.size();
    }

    public void setUserDislikes(List<User> userDislikes) {
        this.userDislikes = userDislikes;
    }

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }
 
    // Other methods

    public boolean addUserLike(User user) {
        return userLikes.add(user);
    }
    
    public boolean removeUserLike(User user) {
        return userLikes.remove(user);
    }

    public boolean addUserDislike(User user) {
        return userDislikes.add(user);
    }
    
    public boolean removeUserDislike(User user) {
        return userDislikes.remove(user);
    }
    
}
