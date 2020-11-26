package controllers.ui.menu.review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ristodroid.R;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Review;

public class ReviewRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final List<Review> reviews;
    private final Context context;


    public ReviewRecycleViewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }


    //facciamo l'inflate (gonfiaggio) lo riportiamo sul ViewHolder -> grazie al quale andrà a richiamare i vari componenti
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_row_review, parent, false);
        return new ViewHolder(v);
    }

    //imposta gli oggetti presi dalla lista popolata da classi "category"
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Review review = reviews.get(position);
        SimpleDateFormat convertStringToDate = new SimpleDateFormat("E, yyyy-MM-dd");

        String score = review.getScore() + "/5";
        ((ViewHolder) holder).scoreTitle.setText(score);
        ((ViewHolder) holder).contentReview.setText(review.getText());
        ((ViewHolder) holder).timestampReview.setText(convertStringToDate.format(review.getReviewData()));
    }

    //restituisce la dimensione della lista
    @Override
    public int getItemCount() {
        return reviews.size();
    }

    //definiamo il viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView scoreTitle;
        private final TextView contentReview;
        private final TextView timestampReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scoreTitle = itemView.findViewById(R.id.textScoreListReview);
            contentReview = itemView.findViewById(R.id.textListReviewContent);
            timestampReview = itemView.findViewById(R.id.text_timestamp_review);
        }
    }

    public void updateListReview(){
        notifyDataSetChanged();
    }
}
