package dto.product;

import java.util.HashMap;
import java.util.Map;

public class IngredientPacket extends Product {
    private double price;
    private String status;
    private Map<Ingredient, Integer> contains;

    public IngredientPacket(String id, String name, String description, boolean isOnSale, int discountID,double discountPercent, double price, String status) {
        super(id, name, description, isOnSale, discountID,discountPercent);
        this.price = price;
        this.status = status;
        this.contains = new HashMap<>();
    }

    
    
    @Override
    public double getPriceAfterDiscount() {
        return isOnSale() ? getPrice()*((100-getDiscountPercent())/100):getPrice();
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

    @Override
    public double getPrice() {
        return contains.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    @Override
    public String getImageURL() {
        return "images/packet/packet.png";
    }
}
