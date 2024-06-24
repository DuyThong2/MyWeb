/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.plan;

import java.io.Serializable;

/**
 *
 * @author ASUS
 */
public class DayPlan implements Serializable {
    private String id;
    private String mealId;
    private String mealPlanId;
    private String customerPlanId;
    private int dayInWeek;
    private int status;
    

    public DayPlan(String id, String mealId, String mealPlanId, String customerPlanId, int dayInWeek, int status) {
        this.id = id;
        this.mealId = mealId;
        this.mealPlanId = mealPlanId;
        this.customerPlanId = customerPlanId;
        this.dayInWeek = dayInWeek;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(String mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public String getCustomerPlanId() {
        return customerPlanId;
    }

    public void setCustomerPlanId(String customerPlanId) {
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
    
    
}
