package students.polsl.eatnear.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import students.polsl.eatnear.BuildConfig;

/**
 * Tool class used to generate veratile retrofit objects used in requests
 */
public class RetrofitUtils {
    /**
     * Method used to compose client object for retrofit.
     * @param baseUrl url used for all requests
     * @param client type of client that defines types of requests
     * @param <T> type of client composed
     * @return client used in retrofit requests
     */
    public static <T> T createClient(String baseUrl, Class<T> client){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (BuildConfig.DEBUG)
            httpClientBuilder.addInterceptor(loggingInterceptor);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build());//logging client
        Retrofit retrofit = builder.build();
        return retrofit.create(client);
    }
}
