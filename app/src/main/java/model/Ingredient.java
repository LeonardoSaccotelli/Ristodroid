package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import controllers.Utility;

public class Ingredient implements Parcelable {
    private int id;
    private String name;


    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Ingredient(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static String getIngredientsToString (List<Ingredient> ingredients) {
        ArrayList<String> list = new ArrayList<>();
        for(int i=0; i<ingredients.size(); i++) {
            list.add(ingredients.get(i).getName());
        }
        return Utility.listToStringWithDelimiter(list, ", ");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
