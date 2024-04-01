package es.codeurjc.backend.dto;

public class PostAddDTO {
    private String text;
    private byte[] imageFile;
    private Long threadId;
    private boolean liked;
    private boolean disliked;
    private boolean reported;

    // Constructor
    public PostAddDTO(String text, String imageFile, Long threadId, boolean liked, boolean disliked, boolean reported) {
        this.text = text;
        if (imageFile != null) {
            this.imageFile = imageFile.getBytes();
        }
        this.threadId = threadId;
        if (liked ^ disliked) {
            this.liked = liked;
            this.disliked = disliked;
        } else {
            this.liked = false;
            this.disliked = false;
        }
        this.reported = reported;
    }

    // Getters
    public String getText() {
        return text;
    }

    public byte[] getImageFile() {
        return imageFile;
    }

    public Long getThreadId() {
        return threadId;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isDisliked() {
        return disliked;
    }

    public boolean isReported() {
        return reported;
    }

    // Setters
    public void setText(String text) {
        this.text = text;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }
}
