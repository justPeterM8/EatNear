package students.polsl.eatnear.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import students.polsl.eatnear.R;
import students.polsl.eatnear.model.Restaurant;


public class RestaurantsMainAdapter extends RecyclerView.Adapter<RestaurantsMainAdapter.RestaurantTileViewHolder> {
    private RestaurantTileListener restaurantListener;
    private Context appContext;
    private List<Restaurant> data;

    public interface RestaurantTileListener{
        void onClickAction(View view, double[] latLong);
    }

    public RestaurantsMainAdapter(RestaurantTileListener listener, Context context){
        this.restaurantListener = listener;
        this.appContext = context;
    }

    @Override
    public RestaurantTileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(appContext);
        View view = layoutInflater.inflate(R.layout.restaurant_list, parent, false);
        return new RestaurantTileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RestaurantTileViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setTag(R.id.addressTextView, data.get(position).getAddress());
        holder.itemView.setTag(R.id.restaurantNameTextView, data.get(position).getName());
        holder.setLocalizationSpecifics(data.get(position).getLocalizationLatitude(),
                data.get(position).getLocalizationLongitude());
        holder.restaurantNameTW.setText(data.get(position).getName());
        holder.restaurantDistanceTW.setText("Distance: " + data.get(position).getDistance());
        holder.restaurantRating.setRating(data.get(position).getOverallRating());
    }

    public void swapData(List<Restaurant> data){
        this.data = data;
        if (this.data != null)
            this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class RestaurantTileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView restaurantNameTW;
        private TextView restaurantDistanceTW;
        private RatingBar restaurantRating;
        private double[] latLong;

        public RestaurantTileViewHolder(View itemView) {
            super(itemView);
            this.restaurantNameTW = itemView.findViewById(R.id.restaurantNameTextView);
            this.restaurantDistanceTW = itemView.findViewById(R.id.distanceTextView);
            this.restaurantRating = itemView.findViewById(R.id.restaurantRatingBar);
            itemView.setOnClickListener(this);
        }
        private void setLocalizationSpecifics(double latitude, double longitude){
            this.latLong = new double[] {latitude, longitude};
        }

        @Override
        public void onClick(View view) {
            restaurantListener.onClickAction(view, latLong);
        }
    }
}
