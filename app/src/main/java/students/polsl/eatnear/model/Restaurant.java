package students.polsl.eatnear.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurant {
    @SerializedName("name")
    private String name;

    @SerializedName("localizationLongitude")
    private String localizationLongitude;

    @SerializedName("localizationLatitude")
    private String localizationLatitude;

    @SerializedName("distance")
    private String distance;

    @SerializedName("rating")
    private float rating;

    @SerializedName("reviews")
    List<Review> reviews;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getLocalizationLongitude() {
        return localizationLongitude;
    }

    public void setLocalizationLongitude(String localizationLongitude) {
        this.localizationLongitude = localizationLongitude;
    }

    public String getLocalizationLatitude() {
        return localizationLatitude;
    }

    public void setLocalizationLatitude(String localizationLatitude) {
        this.localizationLatitude = localizationLatitude;
    }


    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                ", name='" + name + '\'' +
                ", distance=" + distance +
                ", rating=" + rating +
                '}';
    }
}
