package students.polsl.eatnear.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import students.polsl.eatnear.R;
import students.polsl.eatnear.model.Review;


public class ReviewsMainAdapter extends RecyclerView.Adapter<ReviewsMainAdapter.ReviewTileViewHolder> {
    private Context appContext;
    private List<Review> data;


    public ReviewsMainAdapter(Context context){
        this.appContext = context;
    }

    @Override
    public ReviewTileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(appContext);
        View view = layoutInflater.inflate(R.layout.customer_reviews_list, parent, false);
        return new ReviewTileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewTileViewHolder holder, int position) {
        holder.reviewAuthorTW.setText(data.get(position).getAuthor());
        holder.reviewDescriptionTW.setText(data.get(position).getDescription());
        holder.reviewDateTW.setText("Date: " + (data.get(position).getDate()));
        holder.reviewRating.setRating((float) data.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void swapData(List<Review> data){
        this.data = data;
        if (this.data != null)
            this.notifyDataSetChanged();
    }

    class ReviewTileViewHolder extends RecyclerView.ViewHolder{
        private TextView reviewAuthorTW;
        private TextView reviewDescriptionTW;
        private TextView reviewDateTW;
        private RatingBar reviewRating;

        public ReviewTileViewHolder(View itemView) {
            super(itemView);
            this.reviewAuthorTW = itemView.findViewById(R.id.reviewAuthor);
            this.reviewDescriptionTW = itemView.findViewById(R.id.reviewDescription);
            this.reviewDateTW = itemView.findViewById(R.id.reviewDate);
            this.reviewRating = itemView.findViewById(R.id.reviewRatingBar);
        }
    }
}
