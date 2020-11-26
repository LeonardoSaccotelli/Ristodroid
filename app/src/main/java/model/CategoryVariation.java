package model;

import java.util.Objects;

public class CategoryVariation {
    private Category category;
    private Variation variation;

    public CategoryVariation(Category category, Variation variation) {
        this.category = category;
        this.variation = variation;
    }

    public Category getCategory() {
        return category;
    }

    public Variation getVariation() {
        return variation;
    }

    @Override
    public String toString() {
        return "CategoryVariation{" +
                "category=" + category +
                ", variation=" + variation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryVariation)) return false;
        CategoryVariation that = (CategoryVariation) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(variation, that.variation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, variation);
    }
}
