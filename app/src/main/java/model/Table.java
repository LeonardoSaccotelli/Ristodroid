package model;

import java.util.List;
import java.util.Objects;

public class Table {
    private int id;
    private List<Order> orderList;

    public Table(int id, List<Order> orderList) {
        this.id = id;
        this.orderList = orderList;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;
        Table table = (Table) o;
        return id == table.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
