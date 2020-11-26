package controllers.ui.menu.review;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ristodroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import controllers.Dashboard;
import model.Dish;
import model.Review;

public class ReviewFragment extends Fragment {
    private Dish dish;
    private BottomNavigationView navMenu;
    private RecyclerView reviewRecyclerView;
    private ReviewRecycleViewAdapter adapter;
    private TextView textAverageScore;
    private RatingBar starAverageScore;
    private TextView textNumberOfReview;
    private ExtendedFloatingActionButton buttonEnterReview;
    private List<Review> reviews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            dish = getArguments().getParcelable("KEY_DISH__REVIEW_BUNDLE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_review, container, false);
        ((Dashboard) getActivity()).getSupportActionBar().setTitle(R.string.review_section_title);
        setHasOptionsMenu(true);
        View dashboardView = getActivity().findViewById(R.id.dashboardView);
        navMenu = dashboardView.findViewById(R.id.nav_view);
        reviewRecyclerView = root.findViewById(R.id.review_recycler_view);

        textAverageScore = root.findViewById(R.id.text_average_score_review_list);
        textAverageScore.setText(Review.averageScore(dish.getListReview()));

        starAverageScore = root.findViewById(R.id.reviewlist_ReviewStarIcon);
        starAverageScore.setRating(Float.parseFloat(Review.averageScore(dish.getListReview())));

        textNumberOfReview = root.findViewById(R.id.text_number_review_list);

        Resources resource = getResources();
        String numberOfReview = dish.getListReview().size() +" "+
                resource.getQuantityString(R.plurals.numberOfReview,
                        dish.getListReview().size());
        textNumberOfReview.setText(numberOfReview);

        buttonEnterReview = root.findViewById(R.id.wrtieReviewButton);

        buttonEnterReview.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putParcelable("KEY_DISH__REVIEW_BUNDLE",dish);
            Navigation.findNavController(v)
                    .navigate(R.id.action_reviewFragment_to_enterReviewFragment,bundle);
        });

        reviews = dish.getListReview();

        if (reviews.size() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            reviewRecyclerView.setLayoutManager(linearLayoutManager);
            adapter = new ReviewRecycleViewAdapter(reviews, getContext());
            reviewRecyclerView.setAdapter(adapter);
            reviewRecyclerView.setHasFixedSize(true);
        }

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_review_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_order_date_asc_review:
                orderReviewByLowerDate();
                return true;
            case R.id.action_order_date_desc_review:
                orderReviewByHigherDate();
                return true;
            case R.id.action_order_score_asc_review:
                orderReviewByLowerScore();
                return true;
            case R.id.action_order_score_desc_review:
                orderReviewByHigherScore();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void orderReviewByLowerDate(){
        reviews.sort((o1, o2) -> o1.getReviewData().compareTo(o2.getReviewData()));
        adapter.updateListReview();
    }

    private void orderReviewByHigherDate(){
        reviews.sort((o1, o2) -> o1.getReviewData().compareTo(o2.getReviewData()));
        Collections.reverse(reviews);
        adapter.updateListReview();
    }

    private void orderReviewByLowerScore(){
        reviews.sort(((o1, o2) -> Integer.compare(o1.getScore(),
                o2.getScore())));
        adapter.updateListReview();
    }

    private void orderReviewByHigherScore(){
        reviews.sort(((o1, o2) -> Integer.compare(o1.getScore(),
                o2.getScore())));
        Collections.reverse(reviews);
        adapter.updateListReview();
    }

}
