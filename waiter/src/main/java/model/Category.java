package model;


import java.util.Objects;

public class Category {

    private int id;
    private String name;
    private byte[] photo;



    public Category(int id, String name, byte[] photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
