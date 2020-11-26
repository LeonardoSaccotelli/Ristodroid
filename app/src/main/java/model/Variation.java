package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import controllers.Utility;
import persistence.RistodroidDBSchema;
import persistence.SqLiteDb;

public class Variation {
    public final static int MINUS_VARIATION = 1;
    public final static int PLUS_VARIATION = 2;
    private int id;
    private String name;
    private double price;
    private List<CategoryVariation> categoryVariations;
    private List<VariationDishOrder> variationDishOrders;

    public Variation(int id, String name, double price, List<CategoryVariation> categoryVariations, List<VariationDishOrder> variationDishOrders) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryVariations = categoryVariations;
        this.variationDishOrders = variationDishOrders;
    }

    public static ArrayList<Variation> getVariations(Context context, int category, List<Ingredient> ingredientList, int variation_type) {

        ArrayList<Variation> variations = new ArrayList<>();

        SQLiteDatabase db = new SqLiteDb(context).getReadableDatabase();

        String queryVariationTable = "SELECT * FROM " + RistodroidDBSchema.VariationTable.NAME + " INNER JOIN "
                + RistodroidDBSchema.CategoryVariationTable.NAME + " ON "
                + RistodroidDBSchema.CategoryVariationTable.Cols.VARIATION + " = "
                + RistodroidDBSchema.VariationTable.Cols.ID
                + " WHERE " + RistodroidDBSchema.CategoryVariationTable.Cols.CATEGORY + "=" + category;

        ArrayList<String> list = new ArrayList<>();

        for(int i=0; i < ingredientList.size(); i++) {
            list.add(ingredientList.get(i).getName());
        }
        
        Cursor variationsCursor = db.rawQuery(queryVariationTable, null);

        while (variationsCursor.moveToNext()) {

            int id = variationsCursor.getInt(variationsCursor.getColumnIndex(RistodroidDBSchema.VariationTable.Cols.ID));
            String name = variationsCursor.getString(variationsCursor.getColumnIndex(RistodroidDBSchema.VariationTable.Cols.NAME));
            double price = variationsCursor.getDouble(variationsCursor.getColumnIndex(RistodroidDBSchema.VariationTable.Cols.PRICE));

            if(list.contains(name) && variation_type == MINUS_VARIATION) {
                variations.add(new Variation(id, name, price, null, null));
            }
            if(!list.contains(name) && variation_type == PLUS_VARIATION) {
                variations.add(new Variation(id, name, price, null, null));
            }
        }

        variationsCursor.close();
        return variations;
    }

   public static ArrayList<Variation> getVAriationByCategory(Context context, int category){
        ArrayList<Variation> variations = new ArrayList<>();
        SQLiteDatabase db = new SqLiteDb(context).getReadableDatabase();

        String queryVariationCategoryTable = "SELECT " + RistodroidDBSchema.CategoryVariationTable.Cols.VARIATION
                + " FROM " + RistodroidDBSchema.VariationTable.NAME + " INNER JOIN "
                + RistodroidDBSchema.CategoryVariationTable.NAME + " ON "
                + RistodroidDBSchema.CategoryVariationTable.Cols.VARIATION + " = "
                + RistodroidDBSchema.VariationTable.Cols.ID
                + " WHERE " + RistodroidDBSchema.CategoryVariationTable.Cols.CATEGORY + "=" + category;

       Cursor variationCategoryCursor = db.rawQuery(queryVariationCategoryTable, null);

       while (variationCategoryCursor.moveToNext()) {
           int id = variationCategoryCursor.getInt(variationCategoryCursor.getColumnIndex(RistodroidDBSchema.CategoryVariationTable.Cols.VARIATION));
           Variation myVariation = new Variation(id, null,0,null, null);
           variations.add(myVariation);
       }

       variationCategoryCursor.close();

       return variations;
   }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Variation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variation)) return false;
        Variation variation = (Variation) o;
        return id == variation.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static String getVariationsToString (List<Variation> variations) {
        ArrayList<String> list = new ArrayList<>();
        for(int i=0; i<variations.size(); i++) {
            list.add(variations.get(i).getName());
        }
        return Utility.listToStringWithDelimiter(list, ", ");
    }
}
