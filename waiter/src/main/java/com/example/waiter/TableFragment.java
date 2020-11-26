package com.example.waiter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Table;

public class TableFragment extends Fragment {

    private TextView emptyTable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_table, container, false);
        ((ListOrderDetailActivity) requireActivity()).getSupportActionBar().setTitle(R.string.tableTitleSection);

        RecyclerView tableRecyclerView = root.findViewById(R.id.table_recycle_view);

        List<Table> tables = Table.getTablesFromDb(getContext());

        if(tables.size()!=0) {
            RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
            tableRecyclerView.setLayoutManager(gridLayoutManager);
            TableRecycleViewAdapter adapter = new TableRecycleViewAdapter(tables, getContext());
            tableRecyclerView.setAdapter(adapter);
            tableRecyclerView.setHasFixedSize(true);

            /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                    LinearLayout.VERTICAL);
            tableRecyclerView.addItemDecoration(dividerItemDecoration);

            DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(requireContext(),
                    LinearLayout.HORIZONTAL);
            tableRecyclerView.addItemDecoration(dividerItemDecorationHorizontal);*/

            adapter.onSwitchFragment((Table table) -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("Table_Selected", table);

                FragmentManager fm = requireActivity().getSupportFragmentManager();
                ConfirmFragment confirmFragment = new ConfirmFragment();
                confirmFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.FragmentContainer, confirmFragment).commit();
            });

        }else{
            emptyTable = root.findViewById(R.id.table_empty);
            emptyTable.setVisibility(View.VISIBLE);
            emptyTable.setText(R.string.empty_table);
        }

        return root;
    }
}
