/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.plan;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class CustomerPlan extends MealPlan implements Serializable {

    private int week;
    private LocalDateTime dateApply;

    
    public CustomerPlan(int week, String name, LocalDateTime dateApply, String id, String type, String content, String imgUrl, int status) {
        super(id,name,type, content, imgUrl, status);
        this.week = week;
        this.dateApply = dateApply;
    }

    public CustomerPlan(int week, LocalDateTime dateApply, String id, String name, String type, String content, String imgUrl, int status, List<DayPlan> dayPlanContains) {
        super(id, name, type, content, imgUrl, status, dayPlanContains);
        this.week = week;
        this.dateApply = dateApply;
    }

    
    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public LocalDateTime getDateApply() {
        return dateApply;
    }

    public void setDateApply(LocalDateTime dateApply) {
        this.dateApply = dateApply;
    }

    }
