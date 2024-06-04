/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.product;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Admin
 */
public class Ingredient implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String unit;
    private String imgURL;
    private double price;

    public Ingredient(int id, String name, String unit, String imgURL,double price) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.imgURL = imgURL;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ingredient other = (Ingredient) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

   
    
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Override
    public String toString() {
        return "Ingredient{" + "id=" + id + ", name=" + name + ", unit=" + unit + ", imgURL=" + imgURL + '}';
    }
    
    
    
    
}
