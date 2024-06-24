/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author Admin
 */
public class Main {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dayStart = localDate.with(DayOfWeek.MONDAY);
        LocalDate dayEnd = localDate.with(DayOfWeek.SUNDAY);
        System.out.println(dayStart.getDayOfWeek() + " "+dayStart.format(formatter));
        System.out.println(dayEnd.getDayOfWeek() + " "+dayEnd.format(formatter));
    }
}
