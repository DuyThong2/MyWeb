/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.product;

/**
 *
 * @author Admin
 */
public class Meal extends Product {
    private String content;
    private String category;
    private String imageURL;
    private String status;
    private double price;
    private IngredientPacket packet;

    public Meal() {
    }

    

    public Meal(String id, String name, String decription,double price,
            boolean isOnSale, int discountID,String content, String category, String imageURL, String status) {
        super(id, name, decription, isOnSale, discountID);
        this.content = content;
        this.category = category;
        this.imageURL = imageURL;
        this.status = status;
        this.price = price;
        packet = null;
    }

    public Meal(String id, String name, String description,double price,
            boolean isOnSale, int discountID,String content, String category, String imageURL, String status,IngredientPacket packet) {
        this(id,name,description,price,isOnSale,discountID,content,category,imageURL,status);
        this.packet = packet;
    }
    
    

    public IngredientPacket getPacket() {
        return packet;
    }

    public void setPacket(IngredientPacket packet) {
        this.packet = packet;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
    
    
    
    
    
}
