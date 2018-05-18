package students.polsl.eatnear.fake_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import students.polsl.eatnear.model.Restaurant;

public class FakeRestaurantDataCreator {

    private static final String [] RESTAURANT_NAMES = new String[]{
            "The Champagne Spice",
            "The Lotus",
            "Seawise",
            "After Dark",
            "Limestone",
            "Moonlight",
            "The Eclipse",
            "The Pepper Pizzeria",
            "The Painted Market",
            "The Japanese Bite",
            "The Wall",
            "The Enigma Fussion",
            "Treasure",
            "The Little Factory",
            "Friends",
            "The Honor Fish",
            "The Juniper Shark",
            "Laguna",
            "Purity"
    };

    private static final int [] RESTAURANT_DISTANCES = new int[]{
            100,
            200,
            300,
            400,
            500,
            600,
            700,
            800,
            900,
    };

    private static final float [] RESTAURANT_RATINGS = new float[]{
            2.0f,
            3.0f,
            4.0f,
            5.0f,
            4.5f,
            3.5f,
            2.5f,
            1.5f
    };

    public static List<Restaurant> createRestaurantFakeDataList(int size){
        List<Restaurant> resultList = new ArrayList<>();
        Random rand = new Random(49);
        for(int i=1; i<=size; i++){
            Restaurant restaurant = new Restaurant();
            restaurant.setName(RESTAURANT_NAMES[rand.nextInt(RESTAURANT_NAMES.length)]);
            restaurant.setDistance("Distance: " + RESTAURANT_DISTANCES[rand.nextInt(RESTAURANT_DISTANCES.length)] + "m");
            restaurant.setOverallRating(RESTAURANT_RATINGS[rand.nextInt(RESTAURANT_RATINGS.length)]);
            resultList.add(restaurant);
        }
        return resultList;
    }
}
