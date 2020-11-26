package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

public class Dish implements Parcelable {

    private int id;
    private String name;
    private String description;
    private double price;
    private byte[] photo;
    private Category category;
    private List<Ingredient> ingredientDishes;
    private List<Allergenic> allergenicDishes;

    public Dish(int id, String name, String description, double price, byte[] photo, Category category,
                List<Ingredient> ingredientDishes, List<Allergenic> allergenicDishes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photo = photo;
        this.category = category;
        this.ingredientDishes = ingredientDishes;
        this.allergenicDishes = allergenicDishes;
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

    public double getPrice() {
        return price;
    }

    public byte[] getPhoto() {
        return photo;
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
    }
}
