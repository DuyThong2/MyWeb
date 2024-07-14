/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.plan;

import dto.product.Meal;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
public class CustomerDayPlan implements Serializable {

    private int id;
    private int customerPlanId;
    private int dayInWeek;
    private int status;
    private Meal meal;

    public CustomerDayPlan(int id, int customerPlanId, int dayInWeek, int status, Meal meal) {
        this.id = id;
        this.customerPlanId = customerPlanId;
        this.dayInWeek = dayInWeek;
        this.status = status;
        this.meal = meal;
    }
    public CustomerDayPlan(){
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerPlanId() {
        return customerPlanId;
    }

    public void setCustomerPlanId(int customerPlanId) {
        this.customerPlanId = customerPlanId;
    }

    public int getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(int dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
    
    
}
