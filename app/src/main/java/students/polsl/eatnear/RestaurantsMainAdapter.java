package students.polsl.eatnear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import students.polsl.eatnear.fake_data.Restaurant;


public class RestaurantsMainAdapter extends RecyclerView.Adapter<RestaurantsMainAdapter.RestaurantTileViewHolder> {
    private RestaurantTileListener restaurantListener;
    private Context appContext;
    private final List<Restaurant> fakeDataList;

    public interface RestaurantTileListener{
        void onClickAction(View view);
    }

    public RestaurantsMainAdapter(RestaurantTileListener listener, Context context, List<Restaurant> listOfRestaurants){
        this.restaurantListener = listener;
        this.appContext = context;
        fakeDataList = listOfRestaurants;//filling with fake data
    }

    @Override
    public RestaurantTileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from((Context) appContext);
        View view = layoutInflater.inflate(R.layout.restaurant_list, parent, false);
        return new RestaurantTileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantTileViewHolder holder, int position) {
        holder.itemView.setTag(fakeDataList.get(position).getId());
        holder.itemView.setTag(R.id.addressTextView, "Pyskowice, Armii Krajowej 33");
        holder.itemView.setTag(R.id.restaurantNameTextView, fakeDataList.get(position).getName());

        //using fake data for now
        holder.restaurantNameTW.setText(fakeDataList.get(position).getName());
        holder.restaurantDistanceTW.setText(fakeDataList.get(position).getDistance());
        holder.restaurantRating.setRating(fakeDataList.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return fakeDataList == null ? 0 : fakeDataList.size();
    }

    class RestaurantTileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView restaurantNameTW;
        private TextView restaurantDistanceTW;
        private RatingBar restaurantRating;

        public RestaurantTileViewHolder(View itemView) {
            super(itemView);
            this.restaurantNameTW = itemView.findViewById(R.id.restaurantNameTextView);
            this.restaurantDistanceTW = itemView.findViewById(R.id.distanceTextView);
            this.restaurantRating = itemView.findViewById(R.id.ratingBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            restaurantListener.onClickAction(view);
        }
    }
}
