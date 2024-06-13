package Utility;


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
    
    public static int inputInt(String msg, int max , int min){
        if (min > max){// Swap if the max is smaller than min
            int temp = max;
            max = min;
            min = temp;
        }
        int result = 0;
        boolean cont = false;
        do{// loop until the valid integer is received.
            try {
                System.out.println(msg);
                result = Integer.parseInt(sc.nextLine());
                cont = true;
                if (result <= max && result >= min){
                    cont = false;
                }

            }catch (Exception e){
                System.out.println("input not valid");
                cont = true;
            }
        }while (cont);
        return result;
    }

    public static double inputDouble(String msg, int max , int min){
        if (min > max){// Swap if the max is smaller than min
            int temp = max;
            max = min;
            min = temp;
        }
        double result = 0;
        boolean cont = false;
        do{// loop until the valid integer is received.
            try {
                System.out.println(msg);
                result = Double.parseDouble(sc.nextLine());
                cont = true;
                if (result <= max && result >= min){
                    cont = false;
                }

            }catch (Exception e){
                System.out.println("input not valid");
                cont = true;
            }
        }while (cont);
        return result;
    }
    
    public static int getChoice(Object [] arr){
        //printing the given menu
        for (int i = 0 ; i < arr.length ; i++){
            System.out.println(i+1 + ". "+arr[i]);
        }
        //ask for input choice
        return Tool.inputInt("choose 1.." + arr.length + " : " ,1, arr.length);
    }
    
    
    public static String inputPattern(String msg, String pattern){
        String result;
        do{
            System.out.println(msg);
            result = sc.nextLine().trim();
        }while (!result.matches(pattern));
        return result;
    }
    
    public static String inputNonBlank(String msg){
        String result;
        do{
            result = inputString(msg);
        }while (result.length() == 0);
        return result;
    }
    
    public static String inputString(String msg){
        System.out.println(msg);
        return sc.nextLine().trim();
    }

    public static LocalDateTime inputTime(String msg){
        System.out.println(msg);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime result = null;
        boolean retry = true;
        while (retry){
            try{
                String timeInput = Tool.inputPattern("enter time : ", "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}");
                retry = false;
                result =  LocalDateTime.parse(timeInput,dateTimeFormatter);
            }catch (Exception e){
                retry = true;
            }
        }
        return result;
    }
    
    public static String parseTime(LocalDateTime date){
        if (date == null){
            return "---";
        }else{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd \n HH:mm:ss");
            return date.format(dtf);
        }
    }

    public static boolean checkBox(String msg){
        System.out.println(msg);
        String [] choice = {"Y(Yes)", "N(No)"};
        return Tool.getChoice(choice) != 2;
    }
    
    public static <T> List<List<T>> splitToPage(List<T> listToSplit, int elementPerPage){
        if (listToSplit == null){
            return null;
        }else{
            int size = listToSplit.size();
            int pageQuantity = (int) Math.ceil(size/elementPerPage);
            List<List<T>> result = new ArrayList<>(pageQuantity);
            int index = 0;
            int lastIndex = index+ elementPerPage;
            for (int page = 0 ; page < pageQuantity; page++){
                List<T> pageToAdd = new ArrayList<>(elementPerPage);
                for (int element = index ; element < lastIndex; element++ ){
                    pageToAdd.add(listToSplit.get(element));
                }
                result.add(pageToAdd);
                index = lastIndex;
                lastIndex += elementPerPage;
            }
            
            for (int i = index; i < size;i++){
                List<T> pageToAdd = new ArrayList<>(elementPerPage);
                pageToAdd.add(listToSplit.get(i));
                result.add(pageToAdd);
            }
            
            
            return result;
            
        }
        
    }
    
    


}
