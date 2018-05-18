package students.polsl.eatnear.model;

import com.google.gson.annotations.SerializedName;
public class Review {
    transient private int id;

    @SerializedName("description")
    private String description;

    @SerializedName("author")
    private String author;

    @SerializedName("rating")
    private double rating;

    @SerializedName("date")
    private String date;

    public Review() {
    }

    public Review(String description, String author, double rating, String date) {
        this.description = description;
        this.author = author;
        this.rating = rating;
        this.date = date;
    }

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
