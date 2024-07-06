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
    private int id;
    private String mealId;
    private String mealPlanId;
    private int customerPlanId;
    private int dayInWeek;
    private int status;
    
    public DayPlan(){
        
    }
    public DayPlan(int id, String mealId, String mealPlanId, int customerPlanId, int dayInWeek, int status) {
        this.id = id;
        this.mealId = mealId;
        this.mealPlanId = mealPlanId;
        this.customerPlanId = customerPlanId;
        this.dayInWeek = dayInWeek;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "DayPlan{" + "id=" + id + ", mealId=" + mealId + ", mealPlanId=" + mealPlanId + ", customerPlanId=" + customerPlanId + ", dayInWeek=" + dayInWeek + ", status=" + status + '}';
    }
    
    
}
