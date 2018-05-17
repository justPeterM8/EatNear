package students.polsl.eatnear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import students.polsl.eatnear.model.Review;


public class ReviewMainAdapter extends RecyclerView.Adapter<ReviewMainAdapter.ReviewTileViewHolder> {
    private Context appContext;
    private final List<Review> fakeDataList;


    public ReviewMainAdapter(Context context, List<Review> listOfReviews){
        this.appContext = context;
        fakeDataList = listOfReviews;//filling with fake data
    }

    @Override
    public ReviewTileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from((Context) appContext);
        View view = layoutInflater.inflate(R.layout.customer_reviews_list, parent, false);
        return new ReviewTileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewTileViewHolder holder, int position) {
        //using fake data for now
        holder.reviewAuthorTW.setText(fakeDataList.get(position).getAuthor());
        holder.reviewDescriptionTW.setText(fakeDataList.get(position).getDescription());
        holder.reviewDateTW.setText(fakeDataList.get(position).getDate());
        holder.reviewRating.setRating(fakeDataList.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return fakeDataList == null ? 0 : fakeDataList.size();
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
