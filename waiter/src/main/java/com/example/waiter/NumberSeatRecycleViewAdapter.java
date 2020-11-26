package com.example.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NumberSeatRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> listOfNumberSeat;
    private Context context;
    private SeatNumberSelectedFromDialog seatNumberSelectedFromDialog;

    public NumberSeatRecycleViewAdapter(List<String> numberSeat, Context context){
        this.listOfNumberSeat = numberSeat;
        this.context = context;
    }

    public interface SeatNumberSelectedFromDialog{
        void getSeatFromDialog(String numberSeatSelected);
    }

    public void onSelectedSeatNumberFromDialog(SeatNumberSelectedFromDialog onSelectedSeatItem){
        this.seatNumberSelectedFromDialog = onSelectedSeatItem;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_row_number_seat, parent, false);
        return new NumberSeatRecycleViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final String numberSeat = listOfNumberSeat.get(position);
        ((ViewHolder) holder).seatNumber.setText(numberSeat);

        ((ViewHolder)holder).seatNumber.setOnClickListener(v -> {
            seatNumberSelectedFromDialog.getSeatFromDialog(numberSeat);
        });
    }

    @Override
    public int getItemCount() {
        return listOfNumberSeat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final Button seatNumber;
        private final LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seatNumber = itemView.findViewById(R.id.buttonNumberOfSeat);
            linearLayout = itemView.findViewById(R.id.seatNumberLinearLayout);
        }
    }
}
