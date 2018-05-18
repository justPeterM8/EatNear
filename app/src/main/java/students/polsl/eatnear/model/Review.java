package students.polsl.eatnear.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Review {
    private int id;

    @SerializedName("description")
    private String description;

    @SerializedName("author")
    private String author;

    @SerializedName("rating")
    private float rating;

    @SerializedName("date")
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
