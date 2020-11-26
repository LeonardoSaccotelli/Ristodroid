package model;

import java.util.Objects;

public class VariationDishOrder {
    private Variation variation;
    private OrderDetail orderDetail;

    public VariationDishOrder(Variation variation, OrderDetail orderDetail) {
        this.variation = variation;
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "VariationOrder{" +
                "variation=" + variation +
                ", orderDetail=" + orderDetail +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariationDishOrder)) return false;
        VariationDishOrder that = (VariationDishOrder) o;
        return Objects.equals(variation, that.variation) &&
                Objects.equals(orderDetail, that.orderDetail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variation, orderDetail);
    }
}
