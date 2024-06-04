package dto.product;

import java.util.HashMap;
import java.util.Map;

public class IngredientPacket extends Product {
    private double price;
    private String status;
    private Map<Ingredient, Integer> contains;

    public IngredientPacket(String id, String name, String description, boolean isOnSale, int discountID, double price, String status) {
        super(id, name, description, isOnSale, discountID);
        this.price = price;
        this.status = status;
        this.contains = new HashMap<>();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<Ingredient, Integer> getContains() {
        return contains;
    }

    public void setContains(Map<Ingredient, Integer> contains) {
        this.contains = contains;
    }

    public void addIngredient(Ingredient ingredient, int quantity) {
        contains.merge(ingredient, quantity, Integer::sum);
    }

    public Ingredient getIngredient(int id) {
        return contains.keySet().stream()
                .filter(ingredient -> ingredient.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public double getTotalPrice() {
        return contains.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }
}
