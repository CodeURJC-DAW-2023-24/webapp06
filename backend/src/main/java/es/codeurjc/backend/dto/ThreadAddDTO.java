package es.codeurjc.backend.dto;

import es.codeurjc.backend.model.Forum;

public class ThreadAddDTO {
    private Long forumId;
    private String text;

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}