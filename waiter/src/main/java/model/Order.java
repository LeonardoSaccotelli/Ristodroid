package model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order implements Parcelable {
    private String id;
    private String time;
    private Table table;
    private Seat seat;
    private int seatNumber;
    private boolean confirmed;
    private String extraInfo;
    private List<OrderDetail> orderDetails;


    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
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

    public void setTable(Table table) {
        this.table = table;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Table getTable() {
        return table;
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

    public static String convertToJson(Order o)  {
        Gson gson = new Gson();
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
}
