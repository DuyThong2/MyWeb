/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.plan;

import dto.product.Meal;
import dto.plan.DayPlan;
import java.io.Serializable;
/**
 *
 * @author ASUS
 */
public class DayMeal implements Serializable{
    private DayPlan dayPlan;
    private Meal meal;

    public DayMeal(DayPlan dayPlan, Meal meal) {
        this.dayPlan = dayPlan;
        this.meal = meal;
    }
    public DayMeal(){
        
    }

    // Getters and setters
    public DayPlan getDayPlan() {
        return dayPlan;
    }

    public void setDayPlan(DayPlan dayPlan) {
        this.dayPlan = dayPlan;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @Override
    public String toString() {
        return "DayMeal{" +
                "dayPlan=" + dayPlan +
                ", meal=" + meal +
                '}';
    }
}