import java.util.List;

public class ScenicSpot {
    private String id;
    private String name;
    private String description;
    private String location;
    private double rating;
    private List<String> tags;

    public ScenicSpot() {}

    public ScenicSpot(String id, String name, String description, String location, double rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.rating = rating;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}