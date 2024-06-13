/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.order;


import dao.order.OrderItemDAO;
import dto.account.Address;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Order implements Serializable{
    
    private int orderID;
    private LocalDateTime orderDate;
    private LocalDateTime checkingDate;
    private LocalDateTime abortDate;
    private String status;
    private int customerID;
    private List<OrderItem> orderDetail;
    private Address address;

    public Order() {
    }
    
    

    public Order(int orderID, LocalDateTime orderDate, LocalDateTime checkingDate, LocalDateTime abortDate, String status, int customerID, Address address) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.checkingDate = checkingDate;
        this.abortDate = abortDate;
        this.status = status;
        this.customerID = customerID;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    

    public List<OrderItem> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderItem> orderDetail) {
        this.orderDetail = orderDetail;
    }

    
    

    // Getters and setters for all fields
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getCheckingDate() {
        return checkingDate;
    }

    public void setCheckingDate(LocalDateTime checkingDate) {
        this.checkingDate = checkingDate;
    }

    public LocalDateTime getAbortDate() {
        return abortDate;
    }

    public void setAbortDate(LocalDateTime abortDate) {
        this.abortDate = abortDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    
    public double getTotalPrice(){
        return this.orderDetail.stream().
                mapToDouble(item -> item.getPrice()*item.getQuantity()).
                sum();
    }
    
    public int getTotalItem(){
        return this.orderDetail.stream().mapToInt(OrderItem::getQuantity).sum();
    }
    
    

    
    

    
}
