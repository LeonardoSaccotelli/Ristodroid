package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import persistence.RistodroidDBSchema;
import persistence.SqLiteDb;

public class Seat {
    private int id;
    private String name;

    public double getPrice() {
        return price;
    }

    private double price;

    public Seat(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat seat = (Seat) o;
        return id == seat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static List<Seat> getSeatsFromDb(Context context) {
        SQLiteDatabase db = new SqLiteDb(context).getReadableDatabase();
        List<Seat> seats = new ArrayList<>();

        String[] projection = {
                RistodroidDBSchema.SeatTable.Cols.ID,
                RistodroidDBSchema.SeatTable.Cols.NAME,
                RistodroidDBSchema.SeatTable.Cols.PRICE
        };

        Cursor cursor = db.query(RistodroidDBSchema.SeatTable.NAME, projection, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(RistodroidDBSchema.SeatTable.Cols.ID));
            String name = cursor.getString(cursor.getColumnIndex(RistodroidDBSchema.SeatTable.Cols.NAME));
            double price = cursor.getDouble(cursor.getColumnIndex(RistodroidDBSchema.SeatTable.Cols.PRICE));

            seats.add(new Seat(id, name, price));
        }

        cursor.close();
        return seats;
    }

    public static ArrayList<String> createListOfNumberSeat(){
        ArrayList<String> listOfNumberSeat = new ArrayList<>();
        int maxDefaultSeat = 12;

        for(int i = 0; i < maxDefaultSeat; i++){
            listOfNumberSeat.add(String.valueOf((i+1)));
        }

        return  listOfNumberSeat;
    }

}
