package controllers.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ristodroid.R;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import controllers.Utility;
import model.Variation;

public class VariationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Variation> variations;
    private final Context context;
    private final List<Variation> variationsPlusOrder;
    private final List<Variation> variationsMinusOrder;
    private final int type;



    public VariationRecyclerViewAdapter(List<Variation> variations, Context context, int type) {
        this.variations = variations;
        this.context = context;
        this.variationsPlusOrder = new ArrayList<>();
        this.variationsMinusOrder = new ArrayList<>();
        this.type = type;
    }

    public List<Variation> getVariationsPlusOrder() {
        return variationsPlusOrder;
    }

    public List<Variation> getVariationsMinusOrder() {
        return variationsMinusOrder;
    }

    //facciamo l'inflate (gonfiaggio) lo riportiamo sul ViewHolder -> grazie al quale andrà a richiamare i vari componenti
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_row_variation, parent, false);
        return new VariationRecyclerViewAdapter.ViewHolder(v);
    }


    //imposta gli oggetti presi dalla lista popolata da classi "category"
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        final Variation variation = variations.get(position);

        final String euro = Currency.getInstance(Locale.GERMANY).getSymbol() + " ";
        String price = euro + Utility.priceToString(variation.getPrice());

        ((VariationRecyclerViewAdapter.ViewHolder) holder).variationName.setText(variation.getName());
        if(type==Variation.PLUS_VARIATION) {
            ((VariationRecyclerViewAdapter.ViewHolder) holder).variationPrice.setText(price);
        }
        ((VariationRecyclerViewAdapter.ViewHolder) holder).selected.setOnCheckedChangeListener((buttonView, isChecked) -> {
           managePlusVariation(holder, variation);
           manageMinusVariation(holder, variation);
        });


    }

    private void managePlusVariation(@NonNull final RecyclerView.ViewHolder holder, Variation variation) {
        if(((ViewHolder) holder).selected.isChecked() && type == Variation.PLUS_VARIATION) {
            variationsPlusOrder.add(variation);
        } else {
            if(variationsPlusOrder.contains(variation)) {
                variationsPlusOrder.remove(variation);
            }
        }
    }

    private void manageMinusVariation(@NonNull final RecyclerView.ViewHolder holder, Variation variation) {
        if(((ViewHolder) holder).selected.isChecked() && type == Variation.MINUS_VARIATION) {
            variationsMinusOrder.add(variation);
        } else {
            if(variationsMinusOrder.contains(variation)) {
                variationsMinusOrder.remove(variation);
            }
        }
    }


    //restituisce la dimensione della lista
    @Override
    public int getItemCount() {
        return variations.size();
    }

    //definiamo il viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView variationName;
        private final TextView variationPrice;
        private final CheckBox selected;

        private final LinearLayout variationLinearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            variationName = itemView.findViewById(R.id.variation_name);
            variationPrice = itemView.findViewById(R.id.variation_price);
            selected = itemView.findViewById(R.id.selected);
            variationLinearLayout = itemView.findViewById(R.id.variation_linearLayout);
        }


    }

}

