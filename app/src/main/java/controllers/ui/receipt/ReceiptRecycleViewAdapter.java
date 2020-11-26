package controllers.ui.receipt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import com.example.ristodroid.R;


import java.util.Currency;
import java.util.List;
import java.util.Locale;

import controllers.Utility;
import model.OrderDetail;
import model.Variation;

public class ReceiptRecycleViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<OrderDetail> orderDetailsList;
    private final Context context;

    public ReceiptRecycleViewAdapter(List<OrderDetail> orderDetailsList,Context context){
        this.orderDetailsList = orderDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_row_recepit, parent, false);

        return new ReceiptRecycleViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final OrderDetail detail = orderDetailsList.get(position);
        String euro = Currency.getInstance(Locale.GERMANY).getSymbol() + " ";

        String quantity = detail.getQuantity() + " " + Utility.convertResourceIdToString(R.string.piece, context);
        String total = euro + Utility.priceToString(detail.getQuantity() * (detail.getDish().getPrice() + OrderDetail.getTotalPriceVariation(detail)));

        if (detail.getVariationPlusList().size() != 0) {
            String addVariationString = Utility.convertResourceIdToString(R.string.plusSymbol, context) + " "
                    + Variation.getVariationsToString(detail.getVariationPlusList());
            ((ReceiptRecycleViewAdapter.ViewHolder) holder).textAddVariation.setText(Utility.createIndentedText(addVariationString, 0, 28));
        } else {
            ((ReceiptRecycleViewAdapter.ViewHolder) holder).textAddVariation.setVisibility(View.GONE);
        }

        if (detail.getVariationMinusList().size() != 0) {
            String minusVariationString = Utility.convertResourceIdToString(R.string.minusSymbol, context) + " "
                    + Variation.getVariationsToString(detail.getVariationMinusList());
            ((ReceiptRecycleViewAdapter.ViewHolder) holder).textMinusVariation.setText(Utility.createIndentedText(minusVariationString, 0, 28));
        } else {
            ((ReceiptRecycleViewAdapter.ViewHolder) holder).textMinusVariation.setVisibility(View.GONE);
        }

        ((ReceiptRecycleViewAdapter.ViewHolder) holder).textDishTitle.setText(detail.getDish().getName());
        ((ReceiptRecycleViewAdapter.ViewHolder) holder).textQuantity.setText(quantity);
        ((ReceiptRecycleViewAdapter.ViewHolder) holder).textPrice.setText(total);

    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textDishTitle;
        private final TextView textAddVariation;
        private final TextView textMinusVariation;
        private final TextView textQuantity;
        private final TextView textPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDishTitle = itemView.findViewById(R.id.text_receipt_dish_title);
            textAddVariation = itemView.findViewById(R.id.receipt_text_add_variations);
            textMinusVariation = itemView.findViewById(R.id.receipt_text_minus_variations);
            textQuantity = itemView.findViewById(R.id.receipt_text_quantity_summary);
            textPrice = itemView.findViewById(R.id.receipt_text_dish_price);
        }
    }

}