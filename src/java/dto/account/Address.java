/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.account;

/**
 *
 * @author Admin
 */
public class Address {
    private int customerId;
    private String city;
    private String ward;
    private String street;
    private String district;

    public Address(String city,String district, String ward, String street, int customerId ) {
        this.city = city;
        this.ward = ward;
        this.street = street;
        this.district = district;
        this.customerId = customerId;
    }

    public Address() {
    }
    
    

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s ,%s", street,ward,district,city);
    }
    
    
    
    
}
