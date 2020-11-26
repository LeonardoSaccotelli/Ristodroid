package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.waiter.Utility;

public class Ingredient {
    private int id;
    private String name;


    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
