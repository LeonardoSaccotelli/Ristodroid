package com.example.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import model.Seat;

public class SeatRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<Seat> listOfSeat;
    private List<Seat> listOfSeatFull;
    private Context context;
    private SeatSelectedFromDialog seatSelectedFromDialog;
    private Seat SeatSelected;

    public SeatRecycleViewAdapter(List<Seat> seat, Context context){
        this.listOfSeat = seat;
        this.listOfSeatFull = new ArrayList<>(listOfSeat);
        this.context = context;
    }

    public interface SeatSelectedFromDialog{
        void getSeatFromDialog(Seat seat);
    }

    public void onSelectedSeatFromDialog(SeatSelectedFromDialog onSelectedSeatItem){
        this.seatSelectedFromDialog = onSelectedSeatItem;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_row_seat, parent, false);
        return new SeatRecycleViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Seat seat = listOfSeat.get(position);
        ((ViewHolder) holder).seatName.setText(seat.getName());

        String euro = Currency.getInstance(Locale.GERMANY).getSymbol() + " ";
        String priceSeat = euro + Utility.priceToString(seat.getPrice());
        ((ViewHolder) holder).seatPrice.setText(priceSeat);

        ((ViewHolder)holder).linearLayout.setOnClickListener(v -> {
            seatSelectedFromDialog.getSeatFromDialog(seat);
        });
    }

    @Override
    public int getItemCount() {
        return listOfSeat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView seatName;
        private final TextView seatPrice;
        private final LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seatName = itemView.findViewById(R.id.textNameOfSeat);
            seatPrice = itemView.findViewById(R.id.textPriceOfSeat);
            linearLayout = itemView.findViewById(R.id.seatLinearLayout);
        }
    }

    /*
     * Sezione per la gestione della ricerca
     */
    @Override
    public Filter getFilter() {
        return nameSeatFilter;
    }

    private Filter nameSeatFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Seat> filteredSeatList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredSeatList.addAll(listOfSeatFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Seat seat : listOfSeatFull){
                    if(seat.getName().toLowerCase().contains(filterPattern)){
                        filteredSeatList.add(seat);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredSeatList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listOfSeat.clear();
            listOfSeat.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
