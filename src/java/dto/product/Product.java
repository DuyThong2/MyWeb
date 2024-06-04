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
public class Product implements Serializable{
    private static final long serialVersionUID = 2L;
    private String id;
    private String name;
    private String decription;
    private boolean OnSale;
    private int discountID;
    

    public Product() {
    }
    
    

    public Product(String id, String name, String decription, boolean isOnSale, int discountID) {
        this.id = id;
        this.name = name;
        this.decription = decription;
        this.OnSale = isOnSale;
        this.discountID = discountID;
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
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
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
    
   
    
    
    
}
