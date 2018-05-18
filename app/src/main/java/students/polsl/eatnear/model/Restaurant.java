package students.polsl.eatnear.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurant {
    @SerializedName("name")
    private String name;

    @SerializedName("localizationLongitude")
    private double localizationLongitude;

    @SerializedName("localizationLatitude")
    private double localizationLatitude;

    @SerializedName("distance")
    private String distance;

    @SerializedName("address")
    private String address;

    @SerializedName("overallRating")
    private float overallRating;

    @SerializedName("reviews")
    private List<Review> reviews;

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

    public void setOverallRating(float overallRating) {
        this.overallRating = overallRating;
    }

    public float getOverallRating() {
        return overallRating;
    }

    public double getLocalizationLongitude() {
        return localizationLongitude;
    }

    public void setLocalizationLongitude(double localizationLongitude) {
        this.localizationLongitude = localizationLongitude;
    }

    public double getLocalizationLatitude() {
        return localizationLatitude;
    }

    public void setLocalizationLatitude(double localizationLatitude) {
        this.localizationLatitude = localizationLatitude;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                ", name='" + name + '\'' +
                ", distance=" + distance +
                ", rating=" + overallRating +
                '}';
    }
}
