package model;

import com.example.waiter.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Variation {
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
