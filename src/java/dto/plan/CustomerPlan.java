/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.plan;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class CustomerPlan  implements Serializable {
    private int id;
    private String name;
    private String type;
    private String content;
    private String imgUrl;
    private int status;
    private int customerId;
    private int week;
    private List<CustomerDayPlan> containsCustomerPlan;
    private Date dateApply;

    public CustomerPlan(int id, String name, String type, String content, String imgUrl, int status, int week, List<CustomerDayPlan> containsCustomerPlan, Date dateApply,int customerId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.content = content;
        this.imgUrl = imgUrl;
        this.status = status;
        this.customerId= customerId;
        this.week = week;
        this.containsCustomerPlan = containsCustomerPlan;
        this.dateApply = dateApply;
    }
    public CustomerPlan(){
        
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public List<CustomerDayPlan> getContainsCustomerPlan() {
        return containsCustomerPlan;
    }

    public void setContainsCustomerPlan(List<CustomerDayPlan> containsCustomerPlan) {
        this.containsCustomerPlan = containsCustomerPlan;
    }

    public Date getDateApply() {
        return dateApply;
    }

    @Override
    public String toString() {
        return "CustomerPlan{" + "id=" + id + ", name=" + name + ", type=" + type + ", content=" + content + ", imgUrl=" + imgUrl + ", status=" + status + ", week=" + week + ", containsCustomerPlan=" + containsCustomerPlan + ", dateApply=" + dateApply + '}' +",customerId= "+customerId;
    }

    public void setDateApply(Date dateApply) {
        this.dateApply = dateApply;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    
}   
