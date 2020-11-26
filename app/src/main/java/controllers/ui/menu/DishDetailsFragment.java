package controllers.ui.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ristodroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Currency;
import java.util.Locale;

import controllers.MainActivity;
import controllers.Utility;
import model.Allergenic;
import model.Dish;
import model.Ingredient;
import model.Order;
import model.OrderDetail;
import model.Review;
import model.Variation;


public class DishDetailsFragment extends Fragment {
    private BottomNavigationView navMenu;
    private TextView titleDish;
    private TextView descriptionDish;
    private TextView priceDish;
    private TextView titleIngredient;
    private TextView ingredientDish;
    private TextView titleAllergenic;
    private TextView allergenicDish;
    private ImageView imageView;
    private FloatingActionButton addButton;
    private int quantity;
    private Dish dish;
    private TextView review;
    private RatingBar iconStar;
    private Dialog quantitySelectionDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dish = getArguments().getParcelable("KEY_DISH_BUNDLE");
        if(MainActivity.getOrder()==null) {
            MainActivity.setOrder(new Order(null, null, 0));
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dish_details, container, false);
        View dashboardView = requireActivity().findViewById(R.id.dashboardView);
        navMenu = dashboardView.findViewById(R.id.nav_view);

        String euro = Currency.getInstance(Locale.GERMANY).getSymbol() + " ";

        imageView = root.findViewById(R.id.image_dish_details);
        imageView.setImageBitmap(Utility.byteToBitmap(dish.getPhoto()));

        titleDish = root.findViewById(R.id.text_title_dish_details);
        titleDish.setText(dish.getName());

        descriptionDish = root.findViewById(R.id.text_descption_dish_details);
        descriptionDish.setText(dish.getDescription());

        priceDish = root.findViewById(R.id.text_price_dish_details);
        String stringProceDish = euro + Utility.priceToString(dish.getPrice());
        priceDish.setText(stringProceDish);

        Resources pluralResource = getResources();
        int numIngredient = dish.getIngredientDishes().size();
        titleIngredient= root.findViewById(R.id.text_title_ingredient_list);

        if (numIngredient > 0) {
            titleIngredient.setText(pluralResource.getQuantityString(R.plurals.numberOfIngredients,numIngredient));
        }else{
            titleIngredient.setVisibility(View.GONE);
        }

        ingredientDish = root.findViewById(R.id.text_list_ingrediets_dish_details);
        ingredientDish.setText(Ingredient.getIngredientsToString(dish.getIngredientDishes()));

        int numAllergenic = dish.getAllergenicDishes().size();
        titleAllergenic = root.findViewById(R.id.text_title_allergenic_list);
        if(numAllergenic>0) {
            titleAllergenic.setText(pluralResource.getQuantityString(R.plurals.numberOfAllergenic, numAllergenic));
        }else{
            titleAllergenic.setVisibility(View.GONE);
        }

        allergenicDish = root.findViewById(R.id.text_list_allergenic_dish_details);
        allergenicDish.setText(Allergenic.getAllergenicsToString(dish.getAllergenicDishes()));

        review = root.findViewById(R.id.titleReviewDishDetails);
        String numberOfReviews = "("+dish.getListReview().size()+")";
        review.setText(numberOfReviews);
        review.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            final String key_dish_bundle = "KEY_DISH__REVIEW_BUNDLE";
            bundle.putParcelable(key_dish_bundle, dish);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_dish_details_to_reviewFragment, bundle);
        });

        iconStar = root.findViewById(R.id.dishDetails_ReviewStarIcon);
        iconStar.setRating(Float.parseFloat(Review.averageScore(dish.getListReview())));

        addButton = root.findViewById(R.id.button_CloseOrder);
        addButton.setOnClickListener(v -> {
            if(MainActivity.getOrder().isConfirmed()){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.titleNoAddingDishCloseOrder);
                builder.setMessage(R.string.contentNoAddingDishCloseOrder);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> {

                });

                builder.show();
            }else {
                numberDickerDialog();
            }
        });


        return root;
    }

    private void numberDickerDialog(){

        quantitySelectionDialog = new Dialog(getContext());
        quantitySelectionDialog.setContentView(R.layout.dialog_quantity_dish);
        quantitySelectionDialog.getWindow().setLayout(1000,750);
        quantitySelectionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quantitySelectionDialog.show();
        NumberPicker quantityPicker = quantitySelectionDialog.findViewById(R.id.pickerQuantity);
        quantityPicker.setMaxValue(50);
        quantityPicker.setMinValue(1);
        quantityPicker.setWrapSelectorWheel(true);

        TextView closeDialog = quantitySelectionDialog.findViewById(R.id.closeDialogQuantity);
        closeDialog.setOnClickListener(v1 -> quantitySelectionDialog.dismiss());

        TextView confirmQuantityDialog = quantitySelectionDialog.findViewById(R.id.ConfirmDialogQuantity);
        confirmQuantityDialog.setOnClickListener(v2 -> {
            quantity = quantityPicker.getValue();

            if(Variation.getVAriationByCategory(getContext(), dish.getCategory().getId()).size()>0){
                final String key_dish_bundle = "KEY_DISH_BUNDLE";
                Bundle bundle = new Bundle();
                bundle.putParcelable(key_dish_bundle, dish);
                bundle.putInt("QUANTITY", quantity);
                quantitySelectionDialog.dismiss();
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_dish_details_to_navigation_variation, bundle);
            }else {
                OrderDetail orderDetail = new OrderDetail(MainActivity.getOrder().getId(), dish, quantity);
                MainActivity.getOrder().addToOrder(orderDetail);

                Snackbar.make(navMenu, R.string.addDishToOrder, Snackbar.LENGTH_LONG).setAnchorView(navMenu).show();
                Utility.setSummaryBadge(navMenu);
                quantitySelectionDialog.dismiss();
                Navigation.findNavController(requireView()).navigateUp();
            }
        });
    }
}