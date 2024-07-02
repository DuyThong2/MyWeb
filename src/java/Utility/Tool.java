package Utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class Tool {

    public static Scanner sc = new Scanner(System.in);

    public static void createMenu() {
        System.out.println("1/ add an accuntant\n"
                + "2/iterate all accountants\n"
                + "3/ remove accountain\n"
                + "4/filter all high salary accountant\n"
                + "5/exits");
    }

    public static int inputInt(String msg, int max, int min) {
        if (min > max) {// Swap if the max is smaller than min
            int temp = max;
            max = min;
            min = temp;
        }
        int result = 0;
        boolean cont = false;
        do {// loop until the valid integer is received.
            try {
                System.out.println(msg);
                result = Integer.parseInt(sc.nextLine());
                cont = true;
                if (result <= max && result >= min) {
                    cont = false;
                }

            } catch (Exception e) {
                System.out.println("input not valid");
                cont = true;
            }
        } while (cont);
        return result;
    }

    public static double inputDouble(String msg, int max, int min) {
        if (min > max) {// Swap if the max is smaller than min
            int temp = max;
            max = min;
            min = temp;
        }
        double result = 0;
        boolean cont = false;
        do {// loop until the valid integer is received.
            try {
                System.out.println(msg);
                result = Double.parseDouble(sc.nextLine());
                cont = true;
                if (result <= max && result >= min) {
                    cont = false;
                }

            } catch (Exception e) {
                System.out.println("input not valid");
                cont = true;
            }
        } while (cont);
        return result;
    }

    public static int getChoice(Object[] arr) {
        //printing the given menu
        for (int i = 0; i < arr.length; i++) {
            System.out.println(i + 1 + ". " + arr[i]);
        }
        //ask for input choice
        return Tool.inputInt("choose 1.." + arr.length + " : ", 1, arr.length);
    }

    public static String inputPattern(String msg, String pattern) {
        String result;
        do {
            System.out.println(msg);
            result = sc.nextLine().trim();
        } while (!result.matches(pattern));
        return result;
    }

    public static String inputNonBlank(String msg) {
        String result;
        do {
            result = inputString(msg);
        } while (result.length() == 0);
        return result;
    }

    public static String inputString(String msg) {
        System.out.println(msg);
        return sc.nextLine().trim();
    }

    public static LocalDateTime inputTime(String msg) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime result = null;

        try {
            // Parse the input string to a LocalDate
            LocalDate date = LocalDate.parse(msg, dateTimeFormatter);
            // Convert LocalDate to LocalDateTime by setting the time to midnight
            result = date.atStartOfDay();
        } catch (Exception e) {
            
            result = null;
        }
        System.out.println(result);
        return result;
    }
        
    

    public static String parseTime(LocalDateTime date) {
        if (date == null) {
            return "---";
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd \n HH:mm:ss");
            return date.format(dtf);
        }
    }

    public static boolean checkBox(String msg) {
        System.out.println(msg);
        String[] choice = {"Y(Yes)", "N(No)"};
        return Tool.getChoice(choice) != 2;
    }

    public static <T> List<List<T>> splitToPage(List<T> listToSplit, int elementPerPage) {
        if (listToSplit == null) {
            return null;
        }

        List<List<T>> result = new ArrayList<>();
        int size = listToSplit.size();

        if (size == 0 || elementPerPage <= 0) {
            return result; // Return an empty list of lists
        }

        int pageQuantity = (int) Math.ceil((double) size / elementPerPage);

        for (int i = 0; i < pageQuantity; i++) {
            int start = i * elementPerPage;
            int end = Math.min(start + elementPerPage, size);
            result.add(new ArrayList<>(listToSplit.subList(start, end)));
        }

        return result;
    }

}
