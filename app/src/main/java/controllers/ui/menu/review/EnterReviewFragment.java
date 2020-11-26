package controllers.ui.menu.review;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ristodroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import controllers.Dashboard;
import controllers.Utility;
import model.Dish;
import model.Review;
import persistence.SqLiteDb;


public class EnterReviewFragment extends Fragment {

    private TextView textNameDishReview;
    private EditText editEnterReview;
    private RatingBar ratingBar;
    private BottomNavigationView navMenu;
    private Dish dish;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getParcelable("KEY_DISH__REVIEW_BUNDLE")!= null) {
            dish = getArguments().getParcelable("KEY_DISH__REVIEW_BUNDLE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_enter_review, container, false);
        ((Dashboard) getActivity()).getSupportActionBar().setTitle(R.string.review_section_title);
        View dashboardView = getActivity().findViewById(R.id.dashboardView);
        navMenu = dashboardView.findViewById(R.id.nav_view);
        setHasOptionsMenu(true);


        textNameDishReview = root.findViewById(R.id.text_enter_review);
        String titleEnterReview = Utility.convertResourceIdToString(R.string.title_enter_review, getContext())
                +" "+dish.getName();
        textNameDishReview.setText(titleEnterReview);

        editEnterReview = root.findViewById(R.id.input_enterScore);
        ratingBar = root.findViewById(R.id.star_enter_score);

        FloatingActionButton confirmReview = root.findViewById(R.id.button_confirm_review);
        confirmReview.setOnClickListener(v -> {
            String textReview = editEnterReview.getText().toString();
            int score = (int)ratingBar.getRating();

            if(textReview.length() != 0 &&  score != 0){
                Review newReview = new Review(score,textReview,dish.getId());
                sendReview(newReview);
            }else{
                Toast.makeText(getContext(), R.string.missingReview, Toast.LENGTH_LONG).show();
            }

        });

        return root;
    }

    private void sendReview(Review newReview) {
        RequestQueue rquestQueue = Volley.newRequestQueue(getContext());

        String url = "http://192.168.1.115/Ristodroid/Service/insertReview.php";
        //Create an error listener to handle errors appropriately.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response ->{
            Snackbar.make(navMenu, R.string.ReviewReceive, Snackbar.LENGTH_LONG).setAnchorView(navMenu).show();
            dish.getListReview().add(newReview);

            SQLiteDatabase db = new SqLiteDb(getContext()).getWritableDatabase();
            Review.insertIntoReviewable(db,newReview);

            Bundle bundle = new Bundle();
            bundle.putParcelable("KEY_DISH__REVIEW_BUNDLE",dish);
            Navigation.findNavController(getView()).navigate(R.id.action_enterReviewFragment_to_reviewFragment,bundle);
        },
                error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show()) {

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_REVIEW", newReview.getId());
                params.put("TEXT_REVIEW",newReview.getText());
                params.put("SCORE_REVIEW", String.valueOf(newReview.getScore()));
                params.put("DATE_REVIEW",newReview.getReviewData().toString());
                params.put("DISH_REVIEW", String.valueOf(newReview.getDish()));
                return params;
            }
        };
        rquestQueue.add(stringRequest);
    }
}
