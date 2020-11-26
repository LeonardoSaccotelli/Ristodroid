package model;

import java.sql.Date;
import java.util.Objects;

public class Availability {
    private Menu menu;
    private Dish dish;
    private Date startDate;
    private Date endDate;

    public Availability(Menu menu, Dish dish, Date startDate, Date endDate) {
        this.menu = menu;
        this.dish = dish;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Availability{" +
                "menu=" + menu +
                ", dish=" + dish +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return Objects.equals(menu, that.menu) &&
                Objects.equals(dish, that.dish) &&
                Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, dish, startDate);
    }
}
