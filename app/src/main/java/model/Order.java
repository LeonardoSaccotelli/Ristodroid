package model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import controllers.MainActivity;
import persistence.RistodroidDBSchema;

public class Order implements Parcelable {
    private String id;
    private String time;
    private Table table;
    private Seat seat;
    private int seatNumber;
    private boolean confirmed;
    private List<OrderDetail> orderDetails;

    private String extraInfo;

    public void setId(String id) {
        this.id = id;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Order(Table table, Seat seat, int seatNumber) {
        this.id = UUID.randomUUID().toString();
        this.time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        this.table = table;
        this.seat = seat;
        this.seatNumber = seatNumber;
        this.confirmed = false;
        this.orderDetails = new ArrayList<>();
        this.extraInfo = "";
    }

    protected Order(Parcel in) {
        id = in.readString();
        time = in.readString();
        table = in.readParcelable(Table.class.getClassLoader());
        seat = in.readParcelable(Seat.class.getClassLoader());
        seatNumber = in.readInt();
        extraInfo = in.readString();
        orderDetails = in.readArrayList(OrderDetail.class.getClassLoader());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getId() {
        return id;
    }

    public Seat getSeat() {
        return seat;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", time=" + time +
                ", table=" + table +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    public static void insertIntoJsonOrderTable(SQLiteDatabase db, String order_id, String json) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RistodroidDBSchema.JsonOrderTable.Cols.ID, order_id);
        contentValues.put(RistodroidDBSchema.JsonOrderTable.Cols.JSON, json);
        db.insertWithOnConflict(RistodroidDBSchema.JsonOrderTable.NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String getJsonOrderDb(SQLiteDatabase db, String order_id) {
        String json = null;
        String[] projection = {
                RistodroidDBSchema.JsonOrderTable.Cols.ID,
                RistodroidDBSchema.JsonOrderTable.Cols.JSON
        };

        String[] args = {order_id};

        Cursor cursor = db.query(RistodroidDBSchema.JsonOrderTable.NAME, projection, RistodroidDBSchema.JsonOrderTable.Cols.ID+"=?",
                args, null, null, null);

        while (cursor.moveToNext()) {
            json = cursor.getString(cursor.getColumnIndex(RistodroidDBSchema.JsonOrderTable.Cols.JSON));
        }

        cursor.close();
        return json;
    }

    public static String convertToJson(Order o)  {
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().startsWith("photo");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };

        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(strategy).create();
        String order = gson.toJson(o);
        Log.d("json", order);
        return order;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(time);
        dest.writeValue(table);
        dest.writeValue(seat);
        dest.writeInt(seatNumber);
        dest.writeList(orderDetails);
        dest.writeString(extraInfo);
    }

    public void addToOrder(OrderDetail orderDetail) {
        boolean findDish = false;

        for (int i = 0; i < MainActivity.getOrder().getOrderDetails().size(); i++) {
            if (MainActivity.getOrder().getOrderDetails().get(i).getDish().equals(orderDetail.getDish())) {

                boolean dishSameVariations = MainActivity.getOrder().getOrderDetails().get(i).getVariationPlusList().equals(orderDetail.getVariationPlusList())
                        && MainActivity.getOrder().getOrderDetails().get(i).getVariationMinusList().equals(orderDetail.getVariationMinusList());
                if (dishSameVariations) {
                    MainActivity.getOrder().getOrderDetails().get(i).setQuantity(MainActivity.getOrder().getOrderDetails().get(i).getQuantity() + orderDetail.getQuantity());
                    findDish = true;
                    break;
                }
            }
        }

        if (!findDish) {
            MainActivity.getOrder().getOrderDetails().add(orderDetail);
        }
    }


}
