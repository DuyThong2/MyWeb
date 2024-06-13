package dto.order;


import dto.product.Product;
import java.io.Serializable;


public class OrderItem implements Serializable{
    

    private int orderItemId;
    private int quantity;
    private double price;
    private Product product;

    // Constructors
    public OrderItem() {
    }

    public OrderItem(int orderItemId, int quantity, double price, Product product) {
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }

    // Getters and setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", product=" + product +
                '}';
    }
}
