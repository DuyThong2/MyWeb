/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto.plan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class MealPlan implements Serializable {

    private String id;
    private String type;
    private String content;
    private String imgUrl;
    private int status;
    private List<DayPlan> dayPlanContains;

    public MealPlan(String id, String type, String content, String imgUrl, int status) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.imgUrl = imgUrl;
        this.status = status;
        this.dayPlanContains = new ArrayList<DayPlan>();
    }


    public List<DayPlan> getDayPlanContains() {
        return dayPlanContains;
    }

    public void setDayPlanContains(List<DayPlan> dayPlanContains) {
        this.dayPlanContains = dayPlanContains;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
