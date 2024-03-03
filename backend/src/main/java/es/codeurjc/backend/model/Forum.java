package es.codeurjc.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "forums")
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(nullable = false)
    @JsonManagedReference
    private List<Thread> threads;

    @Column(nullable = false)
    private String icon;

    // Constructors
    public Forum() {
    }

    public Forum(String name, List<Thread> threads, String icon) {
        this.name = name;
        this.threads = threads;
        this.icon = icon;
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

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    // toString method
    @Override
    public String toString() {
        return "Forum{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", threads=" + threads +
                ", icon='" + icon + '\'' +
                '}';
    }
}

