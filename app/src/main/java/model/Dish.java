package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import persistence.RistodroidDBSchema;
import persistence.SqLiteDb;

public class Dish implements Parcelable {

    private int id;
    private String name;
    private String description;
    private double price;
    private byte[] photo;
    private Category category;
    private List<Ingredient> ingredientDishes;
    private List<Allergenic> allergenicDishes;
    private List<Review> listReview;

    public Dish(int id, String name, String description, double price, byte[] photo, Category category,
                List<Ingredient> ingredientDishes, List<Allergenic> allergenicDishes, List<Review> reviews) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photo = photo;
        this.category = category;
        this.ingredientDishes = ingredientDishes;
        this.allergenicDishes = allergenicDishes;
        this.listReview = reviews;
    }


    protected Dish(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        photo = in.createByteArray();
        category = in.readParcelable(Category.class.getClassLoader());
        ingredientDishes = in.readArrayList(Ingredient.class.getClassLoader());
        allergenicDishes = in.readArrayList(Allergenic.class.getClassLoader());
        listReview = in.readArrayList(Review.class.getClassLoader());
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Category getCategory() {
        return category;
    }

    public List<Ingredient> getIngredientDishes() {
        return ingredientDishes;
    }

    public List<Allergenic> getAllergenicDishes() {
        return allergenicDishes;
    }

    public List<Review> getListReview() {
        return listReview;
    }

    public static ArrayList<Dish> getDishes(Context context, int idCategoryRequest) {

        final String DISH_ID ="dishid";
        final String DISH_NAME="dishname";
        final String DISH_DESCR ="dishdescr";
        final String DISH_PRICE ="dishprice";
        final String DISH_PHOTO ="dishphoto";
        final String CATEGORY_ID="categoryid";
        final String CATEGORY_NAME="categoryName";

        ArrayList<Dish> dishes = new ArrayList<>();

        String currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());

        SQLiteDatabase db = new SqLiteDb(context).getReadableDatabase();

        String queryDishAvailabilityTable = "SELECT "
                + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.ID + " AS " + DISH_ID + ","
                + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.NAME + " AS " + DISH_NAME +","
                + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.DESCRIPTION + " AS " + DISH_DESCR +","
                + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.PRICE + " AS " + DISH_PRICE +","
                + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.PHOTO + " AS " + DISH_PHOTO +","
                + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.CATEGORY + " AS " + CATEGORY_ID + ","
                + RistodroidDBSchema.CategoryTable.NAME + "." + RistodroidDBSchema.CategoryTable.Cols.NAME +" AS " + CATEGORY_NAME

                + " FROM " + RistodroidDBSchema.DishTable.NAME + " INNER JOIN "
                + RistodroidDBSchema.CategoryTable.NAME + " ON "
                + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.CATEGORY + " = "
                + RistodroidDBSchema.CategoryTable.NAME + "." + RistodroidDBSchema.CategoryTable.Cols.ID
                + " WHERE " + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.CATEGORY + "=" + idCategoryRequest + " AND "
                + RistodroidDBSchema.DishTable.NAME + "." + RistodroidDBSchema.DishTable.Cols.ID + " IN "
                + "(SELECT " + RistodroidDBSchema.AvailabilityTable.Cols.DISH
                + " FROM " + RistodroidDBSchema.AvailabilityTable.NAME
                + " WHERE (" + RistodroidDBSchema.AvailabilityTable.Cols.ENDDATE + " IS NULL OR "
                + RistodroidDBSchema.AvailabilityTable.Cols.ENDDATE + ">='" + currentDate + "') AND "
                + RistodroidDBSchema.AvailabilityTable.Cols.STARTDATE + "<='" + currentDate +"')";

        String queryIngredientTable = "SELECT * " +
                "FROM " +
                RistodroidDBSchema.IngredientDishTable.NAME + " INNER JOIN " +
                RistodroidDBSchema.IngredientTable.NAME + " ON " +
                RistodroidDBSchema.IngredientDishTable.NAME + "." + RistodroidDBSchema.IngredientDishTable.Cols.INGREDIENT + "=" +
                RistodroidDBSchema.IngredientTable.NAME + "." + RistodroidDBSchema.IngredientTable.Cols.ID;

        String queryAllergenicTable = "SELECT * " +
                "FROM " +
                RistodroidDBSchema.AllergenicDishTable.NAME + " INNER JOIN " +
                RistodroidDBSchema.AllergenicTable.NAME + " ON " +
                RistodroidDBSchema.AllergenicDishTable.NAME + "." + RistodroidDBSchema.AllergenicDishTable.Cols.ALLERGENIC + "=" +
                RistodroidDBSchema.AllergenicTable.NAME + "." + RistodroidDBSchema.AllergenicTable.Cols.ID;


        String queryReviewTable = "SELECT * " +
                "FROM " + RistodroidDBSchema.ReviewTable.NAME;


        Cursor dishesCursor = db.rawQuery(queryDishAvailabilityTable, null);
        Cursor ingredientCursor = db.rawQuery(queryIngredientTable, null);
        Cursor allergenicCursor = db.rawQuery(queryAllergenicTable, null);
        Cursor reviewCursor = db.rawQuery(queryReviewTable, null);

        while (dishesCursor.moveToNext()) {

            int id = dishesCursor.getInt(dishesCursor.getColumnIndex(DISH_ID));
            String name = dishesCursor.getString(dishesCursor.getColumnIndex(DISH_NAME));
            byte[] photo = dishesCursor.getBlob(dishesCursor.getColumnIndex(DISH_PHOTO));
            double price = dishesCursor.getDouble(dishesCursor.getColumnIndex(DISH_PRICE));
            String description = dishesCursor.getString(dishesCursor.getColumnIndex(DISH_DESCR));
            int idCategory = dishesCursor.getInt(dishesCursor.getColumnIndex(CATEGORY_ID));
            String categoryName = dishesCursor.getString(dishesCursor.getColumnIndex(CATEGORY_NAME));

            Category myCategory = new Category(idCategory, categoryName, null);
            ArrayList<Ingredient> myIngredientDish = getIngredients(ingredientCursor, id);
            ArrayList<Allergenic> myAllergenicDish = getAllergenics(allergenicCursor, id);
            ArrayList<Review> myReview = getReview(reviewCursor,id);

            dishes.add(new Dish(id, name, description, price, photo, myCategory, myIngredientDish, myAllergenicDish, myReview));

        }

        allergenicCursor.close();
        dishesCursor.close();
        ingredientCursor.close();
        reviewCursor.close();
        return dishes;
    }

    private static ArrayList<Ingredient> getIngredients(Cursor ingredientCursor, int id) {
        ArrayList<Ingredient> myIngredientDish = new ArrayList<>();
        while (ingredientCursor.moveToNext()) {

            int idDish = ingredientCursor.getInt(ingredientCursor.getColumnIndex(RistodroidDBSchema.IngredientDishTable.Cols.DISH));

            if (id == idDish) {
                Ingredient myNewIngredient = new Ingredient(
                        ingredientCursor.getInt(ingredientCursor.getColumnIndex(RistodroidDBSchema.IngredientTable.Cols.ID)),
                        ingredientCursor.getString(ingredientCursor.getColumnIndex(RistodroidDBSchema.IngredientTable.Cols.NAME)));

                myIngredientDish.add(myNewIngredient);
            }
        }

        ingredientCursor.moveToFirst();
        return myIngredientDish;
    }

    private static ArrayList<Allergenic> getAllergenics(Cursor allergenicCursor, int id) {
        ArrayList<Allergenic> myAllergenicDish = new ArrayList<>();
        while (allergenicCursor.moveToNext()) {
            int idDish = allergenicCursor.getInt(allergenicCursor.getColumnIndex(RistodroidDBSchema.AllergenicDishTable.Cols.DISH));

            if (id == idDish) {
                Allergenic myNewAllergenic = new Allergenic(
                        allergenicCursor.getInt(allergenicCursor.getColumnIndex(RistodroidDBSchema.AllergenicTable.Cols.ID)),
                        allergenicCursor.getString(allergenicCursor.getColumnIndex(RistodroidDBSchema.AllergenicTable.Cols.NAME)));
                myAllergenicDish.add(myNewAllergenic);
            }
        }
        allergenicCursor.moveToFirst();
        return myAllergenicDish;
    }

    private static ArrayList<Review> getReview(Cursor reviewCursor, int id) {
        ArrayList<Review> myReviewList = new ArrayList<>();
        while (reviewCursor.moveToNext()) {
            int idDish = reviewCursor.getInt(reviewCursor.getColumnIndex(RistodroidDBSchema.ReviewTable.Cols.DISH));

            if (id == idDish) {
                SimpleDateFormat convertStringToDate = new SimpleDateFormat("yyyy-MM-dd");
                Date reviewDate = new Date();
                try {
                    reviewDate = convertStringToDate.parse(reviewCursor.getString(reviewCursor.getColumnIndex(RistodroidDBSchema.ReviewTable.Cols.DATE)));
                }catch (ParseException e ){
                    e.printStackTrace();
                }

                Review myNewReview = new Review(
                        reviewCursor.getString(reviewCursor.getColumnIndex(RistodroidDBSchema.ReviewTable.Cols.ID)),
                        reviewCursor.getInt(reviewCursor.getColumnIndex(RistodroidDBSchema.ReviewTable.Cols.SCORE)),
                        reviewCursor.getString(reviewCursor.getColumnIndex(RistodroidDBSchema.ReviewTable.Cols.TEXT)),
                        reviewDate, idDish);
                myReviewList.add(myNewReview);
            }
        }
        reviewCursor.moveToFirst();
        return myReviewList;
    }


    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dish)) return false;
        Dish dish = (Dish) o;
        return id == dish.id;
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeByteArray(photo);
        dest.writeValue(category);
        dest.writeList(ingredientDishes);
        dest.writeList(allergenicDishes);
        dest.writeList(listReview);
    }
}
