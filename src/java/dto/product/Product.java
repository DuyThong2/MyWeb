/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.product;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public abstract class Product implements Serializable{
    private static final long serialVersionUID = 2L;
    private String id;
    private String name;
    private String description;
    private boolean OnSale;
    private int discountID;
    private double discountPercent;
    

    public Product() {
    }
    
    

    public Product(String id, String name, String decription, boolean isOnSale, int discountID,double discountPercent) {
        this.id = id;
        this.name = name;
        this.description = decription;
        this.OnSale = isOnSale;
        this.discountID = discountID;
        this.discountPercent = discountPercent;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }
    
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDecription(String decription) {
        this.description = decription;
    }

    public boolean isOnSale() {
        return OnSale;
    }

    public void setOnSale(boolean isOnSale) {
        this.OnSale = isOnSale;
    }

    public int getDiscountID() {
        return discountID;
    }

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }
    
    public abstract double getPrice();
    public abstract double getPriceAfterDiscount();
    public abstract String getImageURL();
    public abstract boolean canSale();

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", description=" + description + ", OnSale=" + OnSale + ", discountID=" + discountID + '}';
    }
    
   
    
    
    
}
