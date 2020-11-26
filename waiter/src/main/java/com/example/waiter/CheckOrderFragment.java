package com.example.waiter;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import model.OrderDetail;


public class CheckOrderFragment extends Fragment {
    private FloatingActionButton checkOrderButton;
    private List<OrderDetail> details;
    private CheckOrderRecycleViewAdapter adapter;

    public CheckOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_check_order, container, false);

        boolean orderNotEmpty = ListOrderDetailActivity.getOrder()!=null && ListOrderDetailActivity.getOrder().getOrderDetails().size()>0;
        checkOrderButton = root.findViewById(R.id.check_order_floating_button);
        setHasOptionsMenu(true);

        if(orderNotEmpty){

            RecyclerView checkOrderRecyclerView = root.findViewById(R.id.summary_recycler_view);

            details = ListOrderDetailActivity.getOrder().getOrderDetails();

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            checkOrderRecyclerView.setLayoutManager(linearLayoutManager);

            adapter = new CheckOrderRecycleViewAdapter(details,getContext());
            checkOrderRecyclerView.setAdapter(adapter);

            checkOrderRecyclerView.setHasFixedSize(true); //cardview hanno tutte le stesse dimensioni

            checkOrderRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
                @Override
                public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy){
                    if (dy > 0)
                        checkOrderButton.hide();
                    else if (dy < 0)
                        checkOrderButton.show();
                }
            });

            adapter.setOnItemClickListener(new CheckOrderRecycleViewAdapter.manageClickOnButtonCard() {
                @Override
                public void onDeleteClick(int position) {
                    details.remove(position);
                    checkOrderRecyclerView.removeViewAt(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position,details.size());


                    if(details.size() == 0){
                        manageVisibilityOrderEmpty();
                    }
                }

                @Override
                public void onAddQuantityClick(int position) {
                    details.get(position).setQuantity(details.get(position).getQuantity() + 1);
                    adapter.notifyDataSetChanged();
                    Snackbar.make(root,R.string.addDishToOrder,Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onRemoveQuantityClick(int position) {
                    int oldQuantity = details.get(position).getQuantity();

                    if((oldQuantity - 1) > 0){
                        details.get(position).setQuantity(oldQuantity - 1);
                        adapter.notifyDataSetChanged();
                    }else{
                        details.remove(position);
                        checkOrderRecyclerView.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position,details.size());

                        if(details.size() == 0){
                            manageVisibilityOrderEmpty();
                        }
                    }
                    Snackbar.make(root,R.string.removeDishToOrder ,Snackbar.LENGTH_SHORT).show();
                }
            });

        }else{
            manageVisibilityOrderEmpty();
        }

        checkOrderButton.setOnClickListener(v->{
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.FragmentContainer, new TableFragment()).commit();
        });

        return root;
    }


    private void manageVisibilityOrderEmpty (){
        startActivity(new Intent(getContext(), WaiterActivity.class));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_check_order_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete_all_dishes:
                removeAllDishesFromOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeAllDishesFromOrder(){
        if(ListOrderDetailActivity.getOrder()!=null && ListOrderDetailActivity.getOrder().getOrderDetails()!= null) {
            if (ListOrderDetailActivity.getOrder().getOrderDetails().size() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.deleteAllDishRequest);
                builder.setIcon(R.drawable.alert_circle_outline);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                    details.clear();
                    adapter.notifyDataSetChanged();
                    manageVisibilityOrderEmpty();
                });
                builder.setNegativeButton(R.string.cancel, ((dialog, which) -> {
                }));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }
}