package mpdam.android.news;

public class Anime {
    private long id;
    private String title;
    private String imageUrl;
    private String description;
    private Double score;
    private long scored_by;

    private String streaming;

    private String trailer;

    public Anime() {
    }

    public Anime(long id, String title, String imageUrl, String description, Double score, long scored_by, String streaming, String trailer) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.score = score;
        this.scored_by = scored_by;
        this.streaming = streaming;
        this.trailer = trailer;
    }

    public Anime(String streaming) {
        this.streaming = streaming;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public long getScored_by() {
        return scored_by;
    }

    public void setScored_by(long scored_by) {
        this.scored_by = scored_by;
    }

    public String getStreaming() {
        return streaming;
    }

    public void setStreaming(String streaming) {
        this.streaming = streaming;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", score=" + score +
                ", scored_by=" + scored_by +
                ", streaming='" + streaming + '\'' +
                '}';
    }
}
