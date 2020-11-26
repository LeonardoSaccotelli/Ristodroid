package model;

import java.util.List;
import java.util.Objects;

public class Menu {
    private int id;
    private String name;
    private List<Availability> availabilityList;

    public Menu(int id, String name, List<Availability> availabilityList) {
        this.id = id;
        this.name = name;
        this.availabilityList = availabilityList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", availabilityList=" + availabilityList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return id == menu.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
