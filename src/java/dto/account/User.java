/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.account;

import dto.order.Order;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class User implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String email;
    private String pw;
    private String name;
    private Address address;
    private String phone;
    private String imgURL;
    private String status;
    private Map<Integer,Order> orderHistory;
    
    public User(int id, String email, String pw, String name, 
            Address address, String phone, String imgURL, String status,Map<Integer,Order> orders){
        this(id,email,pw,name,address,phone,imgURL,status);
        this.orderHistory = orders;
    }
        

    public User(String name,String phone,String email,String password){
        this(-999999999,email,password,name,null,phone,null,"active");
    }
    public User(){
        this(999999999,"xxx","xxx","xxx",null,"xxx","xxx","xxx");
    }
  
    public User(int id, String email, String pw, String name, Address address, String phone, String imgURL, String status) {

        this.id = id;
        this.email = email;
        this.pw = pw;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.imgURL = imgURL;
        this.status = status;
        orderHistory = null;
    }

    public Map<Integer, Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(Map<Integer, Order> orderHistory) {
        this.orderHistory = orderHistory;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", pw=" + pw + ", name=" + name + ", address=" + address + ", phone=" + phone + ", imgURL=" + imgURL + ", status=" + status + '}';
    }
    
    
    
    
}
