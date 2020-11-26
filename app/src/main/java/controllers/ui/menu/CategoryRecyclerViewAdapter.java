package controllers.ui.menu;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ristodroid.R;

import java.util.List;

import controllers.Utility;
import model.Category;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Category> categories;
    private final Context context;


    public CategoryRecyclerViewAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    //facciamo l'inflate (gonfiaggio) lo riportiamo sul ViewHolder -> grazie al quale andrà a richiamare i vari componenti
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_row_menu_category, parent, false);
        return new ViewHolder(v);
    }

    //imposta gli oggetti presi dalla lista popolata da classi "category"
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Category category = categories.get(position);
        ((ViewHolder) holder).categoryTitle.setText(category.getName());
        ((ViewHolder) holder).categoryImage.setImageBitmap(Utility.byteToBitmap(category.getPhoto()));
        ((ViewHolder) holder).categoriesLinearLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", category.getId());
            bundle.putString("category", category.getName());
            Navigation.findNavController(v)
                    .navigate(R.id.action_navigation_menu_to_dishesFragment, bundle);
        });

    }

    //restituisce la dimensione della lista
    @Override
    public int getItemCount() {
        return categories.size();
    }

    //definiamo il viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryTitle;
        private final ImageView categoryImage;
        private final LinearLayout categoriesLinearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.text_dish_title);
            categoryImage = itemView.findViewById(R.id.image_menu_category);
            categoriesLinearLayout = itemView.findViewById(R.id.categories_linearlayout);
        }


    }




}
