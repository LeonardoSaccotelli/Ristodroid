package persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqLiteDb extends SQLiteOpenHelper {

    public final static String DB_NAME = "ristodroid_reviewdb";
    public final static int VERSION = 1;

    public SqLiteDb(@Nullable Context context)  {
        super(context, DB_NAME, null, VERSION);
    }

    private final static String QUERY_CREATETABLE_ALLERGENIC =
            "" + "CREATE TABLE " + RistodroidDBSchema.AllergenicTable.NAME + "(" +
                    RistodroidDBSchema.AllergenicTable.Cols.ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    RistodroidDBSchema.AllergenicTable.Cols.NAME + " VARCHAR(255) NOT NULL );";

    private final static String QUERY_CREATETABLE_ALLERGENIC_DISH =
            "" + "CREATE TABLE " + RistodroidDBSchema.AllergenicDishTable.NAME + "(" +
                    RistodroidDBSchema.AllergenicDishTable.Cols.DISH + " integer NOT NULL, " +
                    RistodroidDBSchema.AllergenicDishTable.Cols.ALLERGENIC + " integer NOT NULL, PRIMARY KEY (" +
            RistodroidDBSchema.AllergenicDishTable.Cols.DISH + "," + RistodroidDBSchema.AllergenicDishTable.Cols.ALLERGENIC + ")," +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.AllergenicDishTable.Cols.DISH +"`) REFERENCES `"+
                    RistodroidDBSchema.DishTable.NAME +"` (`" + RistodroidDBSchema.DishTable.Cols.ID + "`) ON UPDATE CASCADE, " +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.AllergenicDishTable.Cols.ALLERGENIC +"`) REFERENCES `"+
                    RistodroidDBSchema.AllergenicTable.NAME +"` (`" + RistodroidDBSchema.AllergenicTable.Cols.ID + "`) ON UPDATE CASCADE );";

    private final static String QUERY_CREATETABLE_AVAILABILITY =
            "" + "CREATE TABLE " + RistodroidDBSchema.AvailabilityTable.NAME + "(" +
                    RistodroidDBSchema.AvailabilityTable.Cols.MENU + " integer NOT NULL, " +
                    RistodroidDBSchema.AvailabilityTable.Cols.DISH + " integer NOT NULL, " +
                    RistodroidDBSchema.AvailabilityTable.Cols.STARTDATE + " date NOT NULL, " +
                    RistodroidDBSchema.AvailabilityTable.Cols.ENDDATE + " date NOT NULL, " +
                    "PRIMARY KEY (" +
                    RistodroidDBSchema.AvailabilityTable.Cols.MENU + ", " +
                    RistodroidDBSchema.AvailabilityTable.Cols.DISH + ", " +
                    RistodroidDBSchema.AvailabilityTable.Cols.STARTDATE + "), " +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.AvailabilityTable.Cols.DISH +"`) REFERENCES `"+
                    RistodroidDBSchema.DishTable.NAME +"` (`" + RistodroidDBSchema.DishTable.Cols.ID + "`) ON UPDATE CASCADE, " +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.AvailabilityTable.Cols.MENU +"`) REFERENCES `"+
                    RistodroidDBSchema.MenuTable.NAME +"` (`" + RistodroidDBSchema.MenuTable.Cols.ID + "`) ON UPDATE CASCADE );";

    private final static String QUERY_CREATETABLE_CATEGORY =
            "" + "CREATE TABLE " + RistodroidDBSchema.CategoryTable.NAME + "(" +
                    RistodroidDBSchema.CategoryTable.Cols.ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    RistodroidDBSchema.CategoryTable.Cols.NAME + " VARCHAR(255) NOT NULL, " +
                    RistodroidDBSchema.CategoryTable.Cols.PHOTO + " LONGBLOB NOT NULL );";

    private final static String QUERY_CREATETABLE_CATEGORYVARIATION =
            "" + "CREATE TABLE " + RistodroidDBSchema.CategoryVariationTable.NAME + "(" +
                    RistodroidDBSchema.CategoryVariationTable.Cols.VARIATION + " integer NOT NULL, " +
                    RistodroidDBSchema.CategoryVariationTable.Cols.CATEGORY + " integer NOT NULL, " +
                    "PRIMARY KEY (" +
                    RistodroidDBSchema.CategoryVariationTable.Cols.VARIATION + ", " +
                    RistodroidDBSchema.CategoryVariationTable.Cols.CATEGORY + "), " +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.CategoryVariationTable.Cols.VARIATION +"`) REFERENCES `"+
                    RistodroidDBSchema.VariationTable.NAME +"` (`" + RistodroidDBSchema.VariationTable.Cols.ID + "`) ON UPDATE CASCADE, " +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.CategoryVariationTable.Cols.CATEGORY +"`) REFERENCES `"+
                    RistodroidDBSchema.CategoryTable.NAME +"` (`" + RistodroidDBSchema.CategoryTable.Cols.ID + "`) ON UPDATE CASCADE );";

    private final static String QUERY_CREATETABLE_DISH =
            "" + "CREATE TABLE " + RistodroidDBSchema.DishTable.NAME + "(" +
                    RistodroidDBSchema.DishTable.Cols.ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    RistodroidDBSchema.DishTable.Cols.NAME + " VARCHAR(255) NOT NULL, " +
                    RistodroidDBSchema.DishTable.Cols.DESCRIPTION + " VARCHAR(255) NOT NULL, " +
                    RistodroidDBSchema.DishTable.Cols.PRICE + " DECIMAL(7, 2) NOT NULL, " +
                    RistodroidDBSchema.DishTable.Cols.CATEGORY + " integer NOT NULL, " +
                    RistodroidDBSchema.DishTable.Cols.PHOTO + " LONGBLOB NOT NULL, " +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.DishTable.Cols.CATEGORY +"`) REFERENCES `"+
                    RistodroidDBSchema.CategoryTable.NAME +"` (`" + RistodroidDBSchema.CategoryTable.Cols.ID + "`) ON UPDATE CASCADE );";

    private final static String QUERY_CREATETABLE_INGREDIENT =
            "" + "CREATE TABLE " + RistodroidDBSchema.IngredientTable.NAME + "(" +
                    RistodroidDBSchema.IngredientTable.Cols.ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    RistodroidDBSchema.IngredientTable.Cols.NAME + " VARCHAR(255) NOT NULL );";

    private final static String QUERY_CREATETABLE_INGREDIENT_DISH =
            "" + "CREATE TABLE " + RistodroidDBSchema.IngredientDishTable.NAME + "(" +
                    RistodroidDBSchema.IngredientDishTable.Cols.DISH + " integer NOT NULL, " +
                    RistodroidDBSchema.IngredientDishTable.Cols.INGREDIENT + " integer NOT NULL, PRIMARY KEY (" +
                    RistodroidDBSchema.IngredientDishTable.Cols.DISH + "," + RistodroidDBSchema.IngredientDishTable.Cols.INGREDIENT + ")," +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.IngredientDishTable.Cols.DISH +"`) REFERENCES `"+
                    RistodroidDBSchema.DishTable.NAME +"` (`" + RistodroidDBSchema.DishTable.Cols.ID + "`) ON UPDATE CASCADE, " +
                    "FOREIGN KEY (`"+ RistodroidDBSchema.IngredientDishTable.Cols.INGREDIENT +"`) REFERENCES `"+
                    RistodroidDBSchema.IngredientTable.NAME +"` (`" + RistodroidDBSchema.IngredientTable.Cols.ID + "`) ON UPDATE CASCADE);";

    private final static String QUERY_CREATETABLE_MENU =
            "" + "CREATE TABLE " + RistodroidDBSchema.MenuTable.NAME + "(" +
                    RistodroidDBSchema.MenuTable.Cols.ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    RistodroidDBSchema.MenuTable.Cols.NAME + " VARCHAR(255) NOT NULL );";

    private final static String QUERY_CREATETABLE_VARIATION =
            "" + "CREATE TABLE " + RistodroidDBSchema.VariationTable.NAME + "(" +
                    RistodroidDBSchema.VariationTable.Cols.ID + " integer PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    RistodroidDBSchema.VariationTable.Cols.NAME + " VARCHAR(30) NOT NULL, " +
                    RistodroidDBSchema.VariationTable.Cols.PRICE + " DECIMAL(10, 2) NOT NULL );";

    private final static String QUERY_CREATETABLE_JSONORDER =
            "" + "CREATE TABLE " + RistodroidDBSchema.JsonOrderTable.NAME + "(" +
                    RistodroidDBSchema.JsonOrderTable.Cols.ID + " VARCHAR(50) PRIMARY KEY NOT NULL, " +
                    RistodroidDBSchema.JsonOrderTable.Cols.JSON+ " LONGTEXT NOT NULL );";

    private final static String QUERY_CREATETABLE_REVIEW =
            "" + "CREATE TABLE " + RistodroidDBSchema.ReviewTable.NAME + "(" +
                    RistodroidDBSchema.ReviewTable.Cols.ID + " VARCHAR(50) NOT NULL PRIMARY KEY, " +
                    RistodroidDBSchema.ReviewTable.Cols.SCORE + " INTEGER NOT NULL, " +
                    RistodroidDBSchema.ReviewTable.Cols.TEXT + " VARCHAR(1500) NOT NULL, " +
                    RistodroidDBSchema.ReviewTable.Cols.DISH + " INTEGER NOT NULL," +
                    RistodroidDBSchema.ReviewTable.Cols.DATE + " date NOT NULL, "+

                    "FOREIGN KEY (`"+ RistodroidDBSchema.ReviewTable.Cols.DISH +"`) REFERENCES `"+
                    RistodroidDBSchema.DishTable.NAME +"` (`" + RistodroidDBSchema.DishTable.Cols.ID + "`) ON UPDATE CASCADE);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATETABLE_ALLERGENIC);
        db.execSQL(QUERY_CREATETABLE_MENU);
        db.execSQL(QUERY_CREATETABLE_INGREDIENT);
        db.execSQL(QUERY_CREATETABLE_CATEGORY);
        db.execSQL(QUERY_CREATETABLE_DISH);
        db.execSQL(QUERY_CREATETABLE_ALLERGENIC_DISH);
        db.execSQL(QUERY_CREATETABLE_INGREDIENT_DISH);
        db.execSQL(QUERY_CREATETABLE_AVAILABILITY);
        db.execSQL(QUERY_CREATETABLE_VARIATION);
        db.execSQL(QUERY_CREATETABLE_CATEGORYVARIATION);
        db.execSQL(QUERY_CREATETABLE_JSONORDER);
        db.execSQL(QUERY_CREATETABLE_REVIEW);
    }

    private static String dropTable (String table_name) {
        return "DROP TABLE IF EXISTS " + table_name;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTable(RistodroidDBSchema.AllergenicTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.MenuTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.IngredientTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.CategoryTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.DishTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.AllergenicDishTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.IngredientDishTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.AvailabilityTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.VariationTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.CategoryVariationTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.JsonOrderTable.NAME));
        db.execSQL(dropTable(RistodroidDBSchema.ReviewTable.NAME));
        onCreate(db);
    }
}
