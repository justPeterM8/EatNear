package students.polsl.eatnear.fake_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import students.polsl.eatnear.model.Review;

public class FakeReviewDataCreator {
    private static final String[] REVIEW_AUTHORS = new String[]{
            "Nikolia",
            "Maxima",
            "Dean",
            "Marvina",
            "Robert",
            "James",
            "Johnny",
            "Emiliano",
            "Charlize"
    };

    private static final String[] REVIEW_DESCRIPTION = new String[]{
            "Really good, but could be better",
            "Great food, will visit You again for sure",
            "Didn't enjoy my meal, it was already cold",
            "One of my favourite restaurants, deserved 5 stars",
            "Worst hamburger in my life, cold and undercooked",
            "If you wanna eat something in reasonable price this a place I would totally recommend",
    };

    private static final float[] REVIEW_RATINGS = new float[]{
            2.0f,
            3.0f,
            4.0f,
            5.0f,
            4.5f,
            3.5f,
            2.5f,
            1.5f
    };


    public static String randBetween(int start, int end) {
        String value = String.valueOf(start + (int) Math.round(Math.random() * (end - start)));
        return value.length() == 1 ? ("0" + value) : value;
    }

    private static String generateRandomDate() {
        return String.format("%s-%s-%s", randBetween(2000, 2018), randBetween(1, 12), randBetween(1, 28));
    }

    public static List<Review> createReviewFakeDataList(int size) {
        List<Review> resultList = new ArrayList<>();
        Random rand = new Random(49);
        for (int i = 1; i <= size; i++) {
            Review review = new Review();
            review.setId(i);
            review.setAuthor(REVIEW_AUTHORS[rand.nextInt(REVIEW_AUTHORS.length)]);
            review.setDescription(REVIEW_DESCRIPTION[rand.nextInt(REVIEW_DESCRIPTION.length)]);
            review.setRating(REVIEW_RATINGS[rand.nextInt(REVIEW_RATINGS.length)]);
            review.setDate("Date: " + generateRandomDate());
            resultList.add(review);
        }
        return resultList;
    }
}
