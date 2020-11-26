package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderDetail {
    private String id;
    private String order;
    private Dish dish;
    private int quantity;
    private List<Variation> variationPlusList;
    private List<Variation> variationMinusList;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderDetail(String order, Dish dish) {
        this.id = UUID.randomUUID().toString();;
        this.order = order;
        this.dish = dish;
        this.quantity = 0;
        this.variationPlusList = new ArrayList<>();
        this.variationMinusList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Dish getDish() {
        return dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Variation> getVariationPlusList() {
        return variationPlusList;
    }

    public List<Variation> getVariationMinusList() {
        return variationMinusList;
    }


    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", order=" + order +
                ", dish=" + dish +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetail)) return false;
        OrderDetail that = (OrderDetail) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static double getTotalPriceVariation (OrderDetail detail){
        double total = 0;
        for(int i=0; i<detail.getVariationPlusList().size();i++){
            total = total +detail.getVariationPlusList().get(i).getPrice();
        }
        return total;
    }
}
