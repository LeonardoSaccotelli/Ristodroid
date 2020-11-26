package com.example.waiter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Order;
import model.Seat;
import model.Table;


public class ConfirmFragment extends Fragment {

    private TextView selectedSeatFromSpinner;
    private Dialog seatSelectionDialog;

    private TextView selectedNumberOfSeat;
    private Dialog seatNumberSelectionDialog;

    private EditText extraInfo;

    private Table tableSelected;
    private TextView titleTableSelected;

    private FloatingActionButton confirmButton;

    public ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableSelected = getArguments().getParcelable("Table_Selected");
        ListOrderDetailActivity.getOrder().setTable(tableSelected);
        Objects.requireNonNull(((ListOrderDetailActivity) requireActivity()).getSupportActionBar()).setTitle(R.string.seatInfo);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_confirm, container, false);
        titleTableSelected = root.findViewById(R.id.text_selected_seat);
        String infoSeatForTableSelected = Utility.convertResourceIdToString(R.string.tableNumberEnterInfo, requireContext()) + " " + tableSelected.getId();
        titleTableSelected.setText(infoSeatForTableSelected);

        createSeatTypeDialog(root);
        createSeatNumberDialog(root);
        confirm(root);

        return root;
    }

    private void createSeatNumberDialog(View root){
        List<String> numberOfSeat = Seat.createListOfNumberSeat();

        selectedNumberOfSeat = root.findViewById(R.id.selectumberOfSeatSpinner);

        selectedNumberOfSeat.setOnClickListener(v -> {
            seatNumberSelectionDialog = new Dialog(getContext());
            seatNumberSelectionDialog.setContentView(R.layout.dialog_searchable_number_seat_spinner);
            seatNumberSelectionDialog.getWindow().setLayout(1000,1000);
            seatNumberSelectionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            seatNumberSelectionDialog.show();

            TextView closeDialog = seatNumberSelectionDialog.findViewById(R.id.dialogNumberSeatClose);
            closeDialog.setOnClickListener(v1 -> seatNumberSelectionDialog.dismiss());

            TextView confirmDialog = seatNumberSelectionDialog.findViewById(R.id.dialogNumberSeatOtherNumber);
            confirmDialog.setOnClickListener(v2 ->{
                EditText enterOtherNumberSeat = seatNumberSelectionDialog.findViewById(R.id.textEnterOtherNumberSeat);
                try{
                    int inputSeat = Integer.parseInt(enterOtherNumberSeat.getText().toString());
                    if(inputSeat>0){
                        ListOrderDetailActivity.getOrder().setSeatNumber(inputSeat);
                        Resources resource = getResources();
                        String textNumberOfSeat = inputSeat + " " + resource.getQuantityString(R.plurals.numberOfSeat, inputSeat);
                        selectedNumberOfSeat.setText(textNumberOfSeat);
                    }else{
                        Toast.makeText(getContext(), R.string.notValidNumberSeat, Toast.LENGTH_SHORT).show();
                    }
                }catch(NumberFormatException e){
                    Toast.makeText(getContext(), R.string.notValidNumberSeat, Toast.LENGTH_SHORT).show();
                }finally {
                    seatNumberSelectionDialog.dismiss();
                }

            });

            RecyclerView seatNumberRecyclerView = seatNumberSelectionDialog.findViewById(R.id.listOfNumberSeat);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
            seatNumberRecyclerView.setLayoutManager(gridLayoutManager);

            NumberSeatRecycleViewAdapter adapter = new NumberSeatRecycleViewAdapter(numberOfSeat, getContext());
            seatNumberRecyclerView.setAdapter(adapter);
            seatNumberRecyclerView.setHasFixedSize(true);

            adapter.onSelectedSeatNumberFromDialog(numberSeatSelected -> {
                ListOrderDetailActivity.getOrder().setSeatNumber(Integer.parseInt(numberSeatSelected));
                Resources resource = getResources();
                String textNumberOfSeat = numberSeatSelected + " " +
                        resource.getQuantityString(R.plurals.numberOfSeat, Integer.parseInt(numberSeatSelected));
                selectedNumberOfSeat.setText(textNumberOfSeat);
                seatNumberSelectionDialog.dismiss();
            });
        });
    }

    private void createSeatTypeDialog (View root){
        selectedSeatFromSpinner = root.findViewById(R.id.textselectSeatIntoSpinner);

        List<Seat> seats = Seat.getSeatsFromDb(getContext());

        selectedSeatFromSpinner.setOnClickListener(v -> {
            seatSelectionDialog = new Dialog(getContext());
            seatSelectionDialog.setContentView(R.layout.dialog_searchable_seat_spinner);
            seatSelectionDialog.getWindow().setLayout(1000,1000);
            seatSelectionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            seatSelectionDialog.show();

            EditText search_editText = seatSelectionDialog.findViewById(R.id.textEnterOtherSeatType);
            TextView closeDialog = seatSelectionDialog.findViewById(R.id.dialogSetTypeClose);
            closeDialog.setOnClickListener(v1 -> seatSelectionDialog.dismiss());

            RecyclerView seatRecyclerView = seatSelectionDialog.findViewById(R.id.listOfTypeSeat);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            seatRecyclerView.setLayoutManager(linearLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                    linearLayoutManager.getOrientation());
            seatRecyclerView.addItemDecoration(dividerItemDecoration);

            SeatRecycleViewAdapter adapter = new SeatRecycleViewAdapter(seats, getContext());
            seatRecyclerView.setAdapter(adapter);
            seatRecyclerView.setHasFixedSize(true);

            search_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    adapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            adapter.onSelectedSeatFromDialog(seat -> {
                selectedSeatFromSpinner.setText(seat.getName());
                ListOrderDetailActivity.getOrder().setSeat(seat);
                seatSelectionDialog.dismiss();
            });
        });
    }

    private void confirm(View root) {
        confirmButton = root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {

            extraInfo = root.findViewById(R.id.extraInfoOrder);
            ListOrderDetailActivity.getOrder().setExtraInfo(extraInfo.getText().toString());

            boolean isAllSetted = ListOrderDetailActivity.getOrder().getSeat() != null &&
                    ListOrderDetailActivity.getOrder().getTable() != null &&
                    ListOrderDetailActivity.getOrder().getSeatNumber() != 0;
            if (isAllSetted) {
                String url ="http://192.168.1.115/Ristodroid/Service/insertOrder.php";
                try {
                    insertOrderIntoDb(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ArrayList<String> unselectedFields = checkSelected();
                String fields = Utility.listToStringWithDelimiter(unselectedFields, ", ");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.titleConfirmError);
                builder.setIcon(R.drawable.alert_circle);
                builder.setMessage(getResources().getQuantityString(
                        R.plurals.numberOfFields, unselectedFields.size()) + " " + fields + " " +
                        getResources().getQuantityString(R.plurals.compilated, unselectedFields.size()));

                builder.setPositiveButton(R.string.ok, (dialog, which) -> {

                });

                builder.show();
            }
        });
    }

    private void insertOrderIntoDb(String url) throws JSONException {
        String order = Order.convertToJson(ListOrderDetailActivity.getOrder());
        JSONObject jsonOrder = new JSONObject(order);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonOrder,
                response -> {
                    Toast.makeText(getContext(),R.string.sendOrderToDbSuccess,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(), WaiterActivity.class));
                },

                error -> Toast.makeText(getContext(),R.string.sendOrderToDbFailed,Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(requireContext()).add(jsonRequest);
    }


    private ArrayList<String> checkSelected() {
        ArrayList<String> fields = new ArrayList<>();

        if (ListOrderDetailActivity.getOrder().getSeat() == null) {
            fields.add(getResources().getString(R.string.seatType));
        }
        if (ListOrderDetailActivity.getOrder().getTable() == null) {
            fields.add(getResources().getString(R.string.tableNumber));
        }
        if (ListOrderDetailActivity.getOrder().getSeatNumber() == 0) {
            fields.add(getResources().getString(R.string.seatNumber));
        }
        return fields;
    }
}