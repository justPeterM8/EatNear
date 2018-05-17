package students.polsl.eatnear.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import students.polsl.eatnear.model.Restaurant;

public interface EatNearClient {

    @GET("/allRestaurants")
    Call<List<Restaurant>> getAllRestaurantsInfo(@Query("latitude") double latitude, @Query("longitude") double longitude);
}
