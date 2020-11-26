package com.example.waiter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.Table;

public class TableRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Table> listOfTable;
    private Context context;
    private ChangeFragment changeFragment;
    private Table tableSelected;

    public TableRecycleViewAdapter(List<Table> tables, Context context){
        this.listOfTable = tables;
        this.context = context;
    }

    public interface ChangeFragment{
        void switchFragment(Table table);
    }

    public void onSwitchFragment(ChangeFragment change){
        this.changeFragment = change;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_row_table, parent, false);
        return new TableRecycleViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Table table = listOfTable.get(position);
        ((TableRecycleViewAdapter.ViewHolder) holder).numberTable.setText(table.getId());
        ((TableRecycleViewAdapter.ViewHolder) holder).imageView.setImageResource(R.drawable.table7);
        ((ViewHolder)holder).linearLayout.setOnClickListener(v -> {
            changeFragment.switchFragment(table);
        });
    }

    @Override
    public int getItemCount() {
        return listOfTable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView numberTable;
        private final ImageView imageView;
        private final LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTable = itemView.findViewById(R.id.text_number_table);
            imageView = itemView.findViewById(R.id.tableIcon);
            linearLayout = itemView.findViewById(R.id.table_linearlayout);

        }
    }
}
