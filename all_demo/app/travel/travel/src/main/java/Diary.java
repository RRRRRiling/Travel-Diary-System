import java.util.Date;

public class Diary {
    private String id;
    private String author;
    private String title;
    private String content;
    private Date createTime;
    private Date updateTime;
    private String scenicSpot;

    public Diary() {}

    public Diary(String author, String title, String content, String scenicSpot) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.scenicSpot = scenicSpot;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getScenicSpot() {
        return scenicSpot;
    }

    public void setScenicSpot(String scenicSpot) {
        this.scenicSpot = scenicSpot;
    }
}