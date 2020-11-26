package controllers.ui.summary;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ristodroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import controllers.MainActivity;
import controllers.Utility;
import model.Order;
import model.OrderDetail;
import nfc.SenderActivity;
import persistence.SqLiteDb;

public class SummaryFragment extends Fragment {

    private BottomNavigationView navMenu;
    private TextView emptySummary;
    private FloatingActionButton confirmButton;
    private List<OrderDetail> details;
    private SummaryRecycleViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_summary, container, false);

        boolean orderNotEmpty = MainActivity.getOrder()!=null && MainActivity.getOrder().getOrderDetails().size()>0;
        emptySummary = root.findViewById(R.id.text_empty_summary);

        View dashboardView = getActivity().findViewById(R.id.dashboardView);
        setHasOptionsMenu(true);

        navMenu= dashboardView.findViewById(R.id.nav_view);
        confirmButton = root.findViewById(R.id.button_CloseOrder);

        if(orderNotEmpty){
            emptySummary.setVisibility(View.GONE);

            if(!MainActivity.getOrder().isConfirmed()){
                confirmButton.setVisibility(View.VISIBLE);
            }

            RecyclerView summaryRecyclerView = root.findViewById(R.id.summary_recycler_view);

            details = MainActivity.getOrder().getOrderDetails();

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            summaryRecyclerView.setLayoutManager(linearLayoutManager);

            adapter = new SummaryRecycleViewAdapter(details,getContext());
            summaryRecyclerView.setAdapter(adapter);

            summaryRecyclerView.setHasFixedSize(true); //cardview hanno tutte le stesse dimensioni

            summaryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                    if (dy > 0)
                        confirmButton.hide();
                    else if (dy < 0)
                        confirmButton.show();
                }
            });

            adapter.setOnItemClickListener(new SummaryRecycleViewAdapter.manageClickOnButtonCard() {
                @Override
                public void onDeleteClick(int position) {
                    details.remove(position);
                    summaryRecyclerView.removeViewAt(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position,details.size());

                    Utility.setSummaryBadge(navMenu);

                    if(details.size() == 0){
                        manageVisibilityOrderEmpty();
                    }
                }

                @Override
                public void onAddQuantityClick(int position) {
                    details.get(position).setQuantity(details.get(position).getQuantity() + 1);
                    adapter.notifyDataSetChanged();
                    Utility.setSummaryBadge(navMenu);
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
                        summaryRecyclerView.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position,details.size());

                        if(details.size() == 0){
                            manageVisibilityOrderEmpty();
                        }
                    }
                    Snackbar.make(root,R.string.removeDishToOrder,Snackbar.LENGTH_SHORT).show();
                    Utility.setSummaryBadge(navMenu);
                }
            });


            if(!MainActivity.getOrder().isConfirmed()){
                confirmButton.setOnClickListener(v-> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.closeOrderTitle);
                    builder.setMessage(R.string.confirmOrderMessage);
                    builder.setIcon(R.drawable.check);
                    builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                        MainActivity.getOrder().setConfirmed(true);
                        //Chiamo attraverso l'adapter il metodo che aggiorna la recycle view in modo
                        //da aggiornare tutti i button rendendoli non visibili
                        adapter.HideButtonsAfterConfirm();
                        this.confirmButton.setVisibility(View.INVISIBLE);
                        Utility.setSummaryBadge(navMenu);

                        SQLiteDatabase db = new SqLiteDb(getContext()).getWritableDatabase();
                        String json = Order.convertToJson(MainActivity.getOrder());
                        Order.insertIntoJsonOrderTable(db, MainActivity.getOrder().getId(), json);
                        Intent intent = new Intent(getContext(), SenderActivity.class);
                        intent.putExtra("order", MainActivity.getOrder().getId());
                        startActivity(intent);
                    });
                    builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                });
            }

        }else {
            //Snackbar.make(root,R.string.emptySummary,Snackbar.LENGTH_LONG).show();
            emptySummary.setText(R.string.emptySummary);
        }

        return root;
    }

    private void manageVisibilityOrderEmpty (){
        emptySummary.setText(R.string.emptySummary);
        emptySummary.setVisibility(View.VISIBLE);
        confirmButton.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_summary_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete_all_dishes:
                removeAllDishesFromSummary();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeAllDishesFromSummary(){
        if(MainActivity.getOrder()!=null && MainActivity.getOrder().getOrderDetails()!= null) {
            if (MainActivity.getOrder().getOrderDetails().size() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.deleteAllDishRequest);
                builder.setIcon(R.drawable.alert_circle_outline);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                    details.clear();
                    adapter.notifyDataSetChanged();
                    Utility.setSummaryBadge(navMenu);
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
