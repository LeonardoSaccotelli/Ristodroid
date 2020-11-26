package persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqLiteDb extends SQLiteOpenHelper {

    public final static String DB_NAME = "ristodroid_Waiter.db";
    public final static int VERSION = 1;

    public SqLiteDb(@Nullable Context context)  {
        super(context, DB_NAME, null, VERSION);
    }

    private final static String QUERY_CREATETABLE_SEAT =
            "" + "CREATE TABLE " + RistodroidDBSchema.SeatTable.NAME + "(" +
                    RistodroidDBSchema.SeatTable.Cols.ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    RistodroidDBSchema.SeatTable.Cols.NAME + " VARCHAR(255) NOT NULL, " +
                    RistodroidDBSchema.SeatTable.Cols.PRICE + " DECIMAL(5, 2) NOT NULL );";

    private final static String QUERY_CREATETABLE_TABLE =
            "" + "CREATE TABLE " + RistodroidDBSchema.TableTable.NAME + " (" +
                    RistodroidDBSchema.TableTable.Cols.ID + " VARCHAR(4) PRIMARY KEY NOT NULL );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATETABLE_TABLE);
        db.execSQL(QUERY_CREATETABLE_SEAT);
    }

    private static String dropTable (String table_name) {
        return "DROP TABLE IF EXISTS " + table_name;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTable(RistodroidDBSchema.TableTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.SeatTable.NAME));
        onCreate(db);
    }



}
