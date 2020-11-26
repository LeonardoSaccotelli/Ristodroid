package persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;
import java.util.TreeMap;


public class LoadJson {

    /**
     * creo un'istanza di SQLiteDatabase e chiamo i metodi di supporto per eseguire le query di insert
     *
     * @param tables
     * @param context
     * @throws JSONException
     */
     public static void insertJsonIntoDb (TreeMap<String, JSONArray> tables, Context context) throws JSONException {
         SQLiteDatabase db = new SqLiteDb(context).getWritableDatabase();

         insertIntoTableTable(tables, db);
         insertIntoSeatTable(tables, db);
     }



    private static void insertIntoTableTable(TreeMap<String, JSONArray> tables, SQLiteDatabase db) throws JSONException {
        ContentValues contentValues;

        for(int i = 0; i< Objects.requireNonNull(tables.get(RistodroidDBSchema.TableTable.NAME)).length(); i++) {
            contentValues = getContentTableValue(tables, i);
            db.insertWithOnConflict(RistodroidDBSchema.TableTable.NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }
    private static ContentValues getContentTableValue (TreeMap<String, JSONArray> tables, int count) throws JSONException {
        ContentValues contentValues = new ContentValues();
        JSONArray table = tables.get(RistodroidDBSchema.TableTable.NAME);
        contentValues.put(RistodroidDBSchema.TableTable.Cols.ID, table.getJSONObject(count).getString(RistodroidDBSchema.TableTable.Cols.ID));

        return contentValues;
    }

    private static void insertIntoSeatTable(TreeMap<String, JSONArray> tables, SQLiteDatabase db) throws JSONException {
        ContentValues contentValues;

        for(int i = 0; i< Objects.requireNonNull(tables.get(RistodroidDBSchema.SeatTable.NAME)).length(); i++) {
            contentValues = getContentSeatValue(tables, i);
            db.insertWithOnConflict(RistodroidDBSchema.SeatTable.NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }
    private static ContentValues getContentSeatValue (TreeMap<String, JSONArray> tables, int count) throws JSONException {
        ContentValues contentValues = new ContentValues();
        JSONArray table = tables.get(RistodroidDBSchema.SeatTable.NAME);
        contentValues.put(RistodroidDBSchema.SeatTable.Cols.ID, table.getJSONObject(count).getInt(RistodroidDBSchema.SeatTable.Cols.ID));
        contentValues.put(RistodroidDBSchema.SeatTable.Cols.NAME, table.getJSONObject(count).getString(RistodroidDBSchema.SeatTable.Cols.NAME));
        contentValues.put(RistodroidDBSchema.SeatTable.Cols.PRICE, table.getJSONObject(count).getDouble(RistodroidDBSchema.SeatTable.Cols.PRICE));

        return contentValues;
    }
}
