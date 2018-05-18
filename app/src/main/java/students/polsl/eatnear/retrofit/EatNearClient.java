package students.polsl.eatnear.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import students.polsl.eatnear.model.Restaurant;
import students.polsl.eatnear.model.Review;

public interface EatNearClient {

    @GET("/allRestaurants")
    Call<List<Restaurant>> getAllRestaurantsInfo(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("/nearRestaurants")
    Call<List<Restaurant>> getNearRestaurantsInfo(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("distance") long distance);

    @GET("/reviews")
    Call<List<Review>> getReviewsForSpecificRestaurant(@Query("restaurantName") String restaurantName);

}
