/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.product;

import dto.product.Meal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Utility.JDBCUtil;
import dto.product.IngredientPacket;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class MealDAO {

    public List<Meal> getAllMeals() {
        String sql1 = "SELECT  [Id],[name],[content],[category],[imgURL],[status] "
                + "FROM [PRJ301].[dbo].[Meal]";
        String sql2 = "SELECT [description],[isOnSale],[DiscountID] FROM [PRJ301].[dbo].[Product] where id = ?";
        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";

        List<Meal> listOfMeal = new ArrayList<>();

        try (Connection con = JDBCUtil.getConnection();
                Statement statement1 = con.createStatement();
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3)) {
            ResultSet rs = statement1.executeQuery(sql1);

            if (rs != null) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    double price = getPrice(id);
                    String decription = null;
                    boolean isOnsale = false;
                    int discountID = 0;
                    double discountPercent = 0;
                    // get price and other information in product:

                    st2.setString(1, id);
                    ResultSet rs2 = st2.executeQuery();

                    if (!rs2.next()) {
                        throw new Exception();
                    } else {

                        decription = rs2.getString(1);
                        isOnsale = rs2.getBoolean(2);
                        discountID = rs2.getInt(3);
                        if (discountID != 0) {
                            st3.setInt(1, discountID);
                            ResultSet discountResult = st3.executeQuery();
                            discountResult.next();
                            discountPercent = discountResult.getDouble(1);
                            LocalDateTime dateStart = discountResult.getTimestamp(2).toLocalDateTime();
                            LocalDateTime dateEnd = discountResult.getTimestamp(3).toLocalDateTime();
                            LocalDateTime now = LocalDateTime.now();
                            isOnsale = now.isAfter(dateStart) && now.isBefore(dateEnd) && discountPercent > 0;
                        }

                    }

                    //remain info;
                    String name = rs.getString(2);
                    String content = rs.getString(3);
                    String category = rs.getString(4);
                    String imgURL = rs.getString(5);
                    String status = rs.getString(6);

                    Meal meal = new Meal(id, name, decription, price, isOnsale, discountID, discountPercent, content, category, imgURL, status);
                    listOfMeal.add(meal);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listOfMeal;
    }

    private double getPrice(String id) {
        String sql2 = "select top 1 value from MealPrice where priceID = ? and status ='active'";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st2 = con.prepareStatement(sql2)) {
            st2.setString(1, id);
            ResultSet rs2 = st2.executeQuery();
            if (!rs2.next()) {
                throw new Exception();
            } else {
                return rs2.getDouble(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public List<Meal> getMealsByName(String nameTofind) {
        String sql1 = "SELECT  [Id],[name],[content],[category],[imgURL],[status] "
                + "FROM [PRJ301].[dbo].[Meal]"
                + " where name like ?";
        String sql2 = "SELECT [description],[isOnSale],[DiscountID] FROM [PRJ301].[dbo].[Product] where id = ?";
        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";
        List<Meal> listOfMeal = new ArrayList<>();

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3)) {
            st1.setString(1, "%" + nameTofind + "%");
            ResultSet rs1 = st1.executeQuery();

            if (rs1 != null) {
                while (rs1.next()) {
                    String id = rs1.getString(1);
                    double price = getPrice(id);
                    String decription = null;
                    boolean isOnsale = false;
                    int discountID = 0;
                    double discountPercent = 0;
                    // get price and other information in product:

                    st2.setString(1, id);
                    ResultSet rs2 = st2.executeQuery();

                    if (!rs2.next()) {
                        throw new Exception();
                    } else {

                        decription = rs2.getString(1);
                        isOnsale = rs2.getBoolean(2);
                        discountID = rs2.getInt(3);
                        if (discountID != 0) {
                            st3.setInt(1, discountID);
                            ResultSet discountResult = st3.executeQuery();
                            discountResult.next();
                            discountPercent = discountResult.getDouble(1);
                            LocalDateTime dateStart = discountResult.getTimestamp(2).toLocalDateTime();
                            LocalDateTime dateEnd = discountResult.getTimestamp(3).toLocalDateTime();
                            LocalDateTime now = LocalDateTime.now();
                            isOnsale = now.isAfter(dateStart) && now.isBefore(dateEnd) && discountPercent > 0;
                        }
                    }

                    //remain info;
                    String name = rs1.getString(2);
                    String content = rs1.getString(3);
                    String category = rs1.getString(4);
                    String imgURL = rs1.getString(5);
                    String status = rs1.getString(6);

                    Meal meal = new Meal(id, name, decription, price, isOnsale, discountID, discountPercent, content, category, imgURL, status);
                    listOfMeal.add(meal);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listOfMeal;
    }

    public Set<String> getAllMealsId() {
        Set<String> idSet = new HashSet<>();
        String sql1 = "SELECT  [Id]"
                + "FROM [PRJ301].[dbo].[Meal]";
        try (Connection con = JDBCUtil.getConnection();
                Statement st1 = con.createStatement()) {
            ResultSet rs = st1.executeQuery(sql1);
            while (rs.next()) {
                idSet.add(rs.getString(1));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return idSet;

    }

    public String checkValidId(String id) {
        if (!id.matches("M[0-9]{1,4}")) {
            return "invalid ID pattern";
        } else if (getAllMealsId().contains(id)) {
            return id + "is already exist";
        } else {
            return null;
        }
    }

    public void InsertToTable(Meal mealToInsert) throws Exception {
        String sql1 = "insert into Product (id,name,description,isOnSale,discountID) values (?,?,?,?,?) ";
        String sql2 = "insert into Meal (id,name,content,category,imgURL,status) values (?,?,?,?,?,?) ";

        String sql3 = "insert into MealPrice (priceID,value,status,dateApply) values (?,?,?,?)";
        Connection con = JDBCUtil.getConnection();
        try (PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3);) {
            con.setAutoCommit(false);
            st1.setString(1, mealToInsert.getId());
            st1.setString(2, mealToInsert.getName());
            st1.setString(3, mealToInsert.getDescription());
            st1.setBoolean(4, mealToInsert.isOnSale());
            if (mealToInsert.isOnSale()) {
                st1.setInt(5, mealToInsert.getDiscountID());
            } else {
                st1.setNull(5, Types.INTEGER);
            }

            st2.setString(1, mealToInsert.getId());
            st2.setString(2, mealToInsert.getName());
            st2.setString(3, mealToInsert.getContent());
            st2.setString(4, mealToInsert.getCategory());
            st2.setString(5, mealToInsert.getImageURL());
            st2.setString(6, mealToInsert.getStatus());

            LocalDateTime dtn = LocalDateTime.now();

            st3.setString(1, mealToInsert.getId());
            st3.setDouble(2, mealToInsert.getPrice());
            st3.setString(3, "active");
            st3.setTimestamp(4, Timestamp.valueOf(dtn));

            int[] results = {
                st1.executeUpdate(),
                st2.executeUpdate(),
                st3.executeUpdate()
            };
            for (int result : results) {
                if (result <= 0) {

                    throw new Exception();
                }
            }
            con.commit();
        } catch (Exception ex) {
            con.rollback();
            ex.printStackTrace();
            throw new RuntimeException();
        } finally {
            con.setAutoCommit(true);
            JDBCUtil.closeConnection(con);
        }
    }

    public Meal getMealFromId(String idToFind) {
        Meal meal = null;
        String GET_MEAL_QUERY
                = "SELECT Id, name, content, category, imgURL, status "
                + "FROM Meal WHERE id = ?";
        String GET_PRODUCT_QUERY
                = "SELECT description, isOnSale, DiscountID "
                + "FROM Product WHERE id = ?";
        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(GET_MEAL_QUERY);
                PreparedStatement st2 = con.prepareStatement(GET_PRODUCT_QUERY);
                PreparedStatement st3 = con.prepareStatement(sql3)) {

            // Fetch Meal details
            st1.setString(1, idToFind);
            try (ResultSet rs1 = st1.executeQuery()) {
                if (rs1.next()) {
                    String id = rs1.getString("Id");
                    String name = rs1.getString("name");
                    String content = rs1.getString("content");
                    String category = rs1.getString("category");
                    String imgURL = rs1.getString("imgURL");
                    String status = rs1.getString("status");

                    double discountPercent = 0;
                    // Fetch Product details
                    st2.setString(1, id);
                    try (ResultSet rs2 = st2.executeQuery()) {
                        if (rs2.next()) {
                            String description = rs2.getString("description");
                            boolean isOnSale = rs2.getBoolean("isOnSale");
                            int discountID = rs2.getInt("DiscountID");
                            if (discountID != 0) {
                                st3.setInt(1, discountID);
                                ResultSet discountResult = st3.executeQuery();
                                discountResult.next();
                                discountPercent = discountResult.getDouble(1);
                                LocalDateTime dateStart = discountResult.getTimestamp(2).toLocalDateTime();
                                LocalDateTime dateEnd = discountResult.getTimestamp(3).toLocalDateTime();
                                LocalDateTime now = LocalDateTime.now();
                                isOnSale = now.isAfter(dateStart) && now.isBefore(dateEnd) && discountPercent > 0;
                            }

                            double price = getPrice(id);

                            meal = new Meal(id, name, description, price, isOnSale, discountID, discountPercent, content, category, imgURL, status);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception or handle it as needed
        }

        return meal;
    }

    public void updateMeal(Meal meal) throws Exception {

        String sql1 = "update Meal set name = ?,content = ?,category = ? ,imgURL = ? where Id = ?";
        String sql2 = "update MealPrice set status = 'disable' where priceID = ?";
        String sql3 = "insert into MealPrice (priceID,value,status,dateApply) values (?,?,?,?)";
        String sql4 = "update Product set name = ?,description = ? where id = ?";
        Connection con = JDBCUtil.getConnection();
        try (PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3);
                PreparedStatement st4 = con.prepareStatement(sql4)) {
            con.setAutoCommit(false);

            st1.setString(1, meal.getName());
            st1.setString(2, meal.getContent());
            st1.setString(3, meal.getCategory());
            st1.setString(4, meal.getImageURL());
            st1.setString(5, meal.getId());

            st2.setString(1, meal.getId());

            LocalDateTime dtn = LocalDateTime.now();

            st3.setString(1, meal.getId());
            st3.setDouble(2, meal.getPrice());
            st3.setString(3, "active");
            st3.setTimestamp(4, Timestamp.valueOf(dtn));

            st4.setString(1, meal.getName());
            st4.setString(2, meal.getDescription());
            st4.setString(3, meal.getId());
            int[] results = {
                st1.executeUpdate(),
                st2.executeUpdate(),
                st3.executeUpdate(),
                st4.executeUpdate()
            };

            con.commit();
        } catch (Exception ex) {
            con.rollback();
            ex.printStackTrace();
            throw new RuntimeException();
        } finally {
            con.setAutoCommit(true);
            JDBCUtil.closeConnection(con);
        }
    }

    public Meal getMealFullDetailFromId(String mealId) {
        Meal meal = null;
        meal = this.getMealFromId(mealId);
        IngredientPacketDAO dao = new IngredientPacketDAO();
        IngredientPacket packet = dao.getIngredientPacketFromId("P" + mealId.substring(1));
        meal.setPacket(packet);
        return meal;
    }

    public void setStatusForMeal(String mealId, String status) {
        String sql = "UPDATE [PRJ301].[dbo].[Meal] SET status = ? WHERE id = ?";

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            // Set the parameters for the query
            pstmt.setString(1, status);
            pstmt.setString(2, mealId);

            // Execute the update
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Meal> getCustomerMealList() {
        String sql1 = "SELECT  [Id],[name],[content],[category],[imgURL],[status] "
                + "FROM [PRJ301].[dbo].[Meal] where status = 'active' ";
        String sql2 = "SELECT [description],[isOnSale],[DiscountID] FROM [PRJ301].[dbo].[Product] where id = ?";
        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";

        List<Meal> listOfMeal = new LinkedList<>();

        try (Connection con = JDBCUtil.getConnection();
                Statement statement1 = con.createStatement();
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3)) {
            ResultSet rs = statement1.executeQuery(sql1);

            if (rs != null) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    double price = getPrice(id);
                    String decription = null;
                    boolean isOnsale = false;
                    int discountID = 0;
                    double discountPercent = 0;
                    // get price and other information in product:

                    st2.setString(1, id);
                    ResultSet rs2 = st2.executeQuery();

                    if (!rs2.next()) {
                        throw new Exception();
                    } else {

                        decription = rs2.getString(1);
                        isOnsale = rs2.getBoolean(2);
                        discountID = rs2.getInt(3);
                        if (discountID != 0) {
                            st3.setInt(1, discountID);
                            ResultSet discountResult = st3.executeQuery();
                            discountResult.next();
                            discountPercent = discountResult.getDouble(1);
                            LocalDateTime dateStart = discountResult.getTimestamp(2).toLocalDateTime();
                            LocalDateTime dateEnd = discountResult.getTimestamp(3).toLocalDateTime();
                            LocalDateTime now = LocalDateTime.now();
                            isOnsale = now.isAfter(dateStart) && now.isBefore(dateEnd) && discountPercent > 0;
                        }

                    }

                    //remain info;
                    String name = rs.getString(2);
                    String content = rs.getString(3);
                    String category = rs.getString(4);
                    String imgURL = rs.getString(5);
                    String status = rs.getString(6);

                    Meal meal = new Meal(id, name, decription, price, isOnsale, discountID, discountPercent, content, category, imgURL, status);
                    listOfMeal.add(meal);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listOfMeal;
    }

    public List<Meal> getCustomerMealListByCategory(String category, int quantity) {
        String sql1 = String.format("SELECT top %d [Id],[name],[content],[category],[imgURL],[status] "
                + "FROM [PRJ301].[dbo].[Meal]"
                + " where status='active' and [category] like ?", quantity);
        String sql2 = "SELECT [description],[isOnSale],[DiscountID] FROM [PRJ301].[dbo].[Product] where id = ?";
        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";
        List<Meal> listOfMeal = new ArrayList<>();

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3)) {
            st1.setString(1, "%" + category + "%");
            ResultSet rs1 = st1.executeQuery();

            if (rs1 != null) {
                while (rs1.next()) {
                    String id = rs1.getString(1);
                    double price = getPrice(id);
                    String decription = null;
                    boolean isOnsale = false;
                    int discountID = 0;
                    double discountPercent = 0;
                    // get price and other information in product:

                    st2.setString(1, id);
                    ResultSet rs2 = st2.executeQuery();

                    if (!rs2.next()) {
                        throw new Exception();
                    } else {

                        decription = rs2.getString(1);
                        isOnsale = rs2.getBoolean(2);
                        discountID = rs2.getInt(3);
                        if (discountID != 0) {
                            st3.setInt(1, discountID);
                            ResultSet discountResult = st3.executeQuery();
                            discountResult.next();
                            discountPercent = discountResult.getDouble(1);
                            LocalDateTime dateStart = discountResult.getTimestamp(2).toLocalDateTime();
                            LocalDateTime dateEnd = discountResult.getTimestamp(3).toLocalDateTime();
                            LocalDateTime now = LocalDateTime.now();
                            isOnsale = now.isAfter(dateStart) && now.isBefore(dateEnd) && discountPercent > 0;
                        }

                    }

                    //remain info;
                    String name = rs1.getString(2);
                    String content = rs1.getString(3);

                    String imgURL = rs1.getString(5);
                    String status = rs1.getString(6);

                    Meal meal = new Meal(id, name, decription, price, isOnsale, discountID, discountPercent, content, category, imgURL, status);
                    listOfMeal.add(meal);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listOfMeal;
    }

    public List<Meal> getMealWhichIsOnSale(int quantity) {
        String sql1 = "SELECT [Id], [name], [content], [category], [imgURL], [status] "
                + "FROM [PRJ301].[dbo].[Meal] "
                + "WHERE status = 'active'";
        String sql2 = "SELECT [description], [isOnSale], [DiscountID] FROM [PRJ301].[dbo].[Product] WHERE id = ?";
        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";

        List<Meal> listOfMeal = new ArrayList<>();

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3)) {

            try (ResultSet rs1 = st1.executeQuery()) {
                while (rs1.next()) {
                    String id = rs1.getString(1);
                    double price = getPrice(id);
                    String description = null;
                    boolean isOnSale = false;
                    int discountID = 0;
                    double discountPercent = 0;

                    // Get price and other information in product:
                    st2.setString(1, id);
                    try (ResultSet rs2 = st2.executeQuery()) {
                        if (rs2.next()) {
                            description = rs2.getString(1);
                            isOnSale = rs2.getBoolean(2);
                            discountID = rs2.getInt(3);
                            if (isOnSale && discountID != 0) {
                                st3.setInt(1, discountID);
                                try (ResultSet discountResult = st3.executeQuery()) {
                                    if (discountResult.next()) {
                                        discountPercent = discountResult.getDouble(1);
                                    }
                                }
                            } else {
                                continue; // Skip if the meal is not on sale
                            }
                        } else {
                            throw new SQLException("No product found with ID: " + id);
                        }
                    }

                    // Remaining info from Meal table
                    String name = rs1.getString(2);
                    String content = rs1.getString(3);
                    String imgURL = rs1.getString(5);
                    String status = rs1.getString(6);

                    Meal meal = new Meal(id, name, description, price, isOnSale, discountID, discountPercent, content, rs1.getString(4), imgURL, status);
                    listOfMeal.add(meal);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Collections.shuffle(listOfMeal);
        return listOfMeal.subList(0, quantity + 1);
    }

    public List<Meal> getMealsByNameForCustomer(String nameTofind) {
        String sql1 = "SELECT  [Id],[name],[content],[category],[imgURL],[status] "
                + "FROM [PRJ301].[dbo].[Meal]"
                + " where name like ? and status = 'active'";
        String sql2 = "SELECT [description],[isOnSale],[DiscountID] FROM [PRJ301].[dbo].[Product] where id = ?";
        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";
        List<Meal> listOfMeal = new ArrayList<>();

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3)) {
            st1.setString(1, "%" + nameTofind + "%");
            ResultSet rs1 = st1.executeQuery();

            if (rs1 != null) {
                while (rs1.next()) {
                    String id = rs1.getString(1);
                    double price = getPrice(id);
                    String decription = null;
                    boolean isOnsale = false;
                    int discountID = 0;
                    double discountPercent = 0;
                    // get price and other information in product:

                    st2.setString(1, id);
                    ResultSet rs2 = st2.executeQuery();

                    if (!rs2.next()) {
                        throw new Exception();
                    } else {

                        decription = rs2.getString(1);
                        isOnsale = rs2.getBoolean(2);
                        discountID = rs2.getInt(3);
                        if (discountID != 0) {
                            st3.setInt(1, discountID);
                            ResultSet discountResult = st3.executeQuery();
                            discountResult.next();
                            discountPercent = discountResult.getDouble(1);
                            LocalDateTime dateStart = discountResult.getTimestamp(2).toLocalDateTime();
                            LocalDateTime dateEnd = discountResult.getTimestamp(3).toLocalDateTime();
                            LocalDateTime now = LocalDateTime.now();
                            isOnsale = now.isAfter(dateStart) && now.isBefore(dateEnd) && discountPercent > 0;

                        }
                    }

                    //remain info;
                    String name = rs1.getString(2);
                    String content = rs1.getString(3);
                    String category = rs1.getString(4);
                    String imgURL = rs1.getString(5);
                    String status = rs1.getString(6);

                    Meal meal = new Meal(id, name, decription, price, isOnsale, discountID, discountPercent, content, category, imgURL, status);
                    listOfMeal.add(meal);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listOfMeal;
    }
////       public Meal getMealFromId(String idToFind) {
////        Meal meal = null;
////        String GET_MEAL_QUERY
////                = "SELECT Id, name, content, category, imgURL, status "
////                + "FROM Meal WHERE id = ?";
////        String GET_PRODUCT_QUERY
////                = "SELECT description, isOnSale, DiscountID "
////                + "FROM Product WHERE id = ?";
////        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";
////        try (Connection con = JDBCUtil.getConnection();
////                PreparedStatement st1 = con.prepareStatement(GET_MEAL_QUERY);
////                PreparedStatement st2 = con.prepareStatement(GET_PRODUCT_QUERY);
////                PreparedStatement st3 = con.prepareStatement(sql3)) {
////
////            // Fetch Meal details
////            st1.setString(1, idToFind);
////            try (ResultSet rs1 = st1.executeQuery()) {
////                if (rs1.next()) {
////                    String id = rs1.getString("Id");
////                    String name = rs1.getString("name");
////                    String content = rs1.getString("content");
////                    String category = rs1.getString("category");
////                    String imgURL = rs1.getString("imgURL");
////                    String status = rs1.getString("status");
////
////                    double discountPercent = 0;
////                    // Fetch Product details
////                    st2.setString(1, id);
////                    try (ResultSet rs2 = st2.executeQuery()) {
////                        if (rs2.next()) {
////                            String description = rs2.getString("description");
////                            boolean isOnSale = rs2.getBoolean("isOnSale");
////                            int discountID = rs2.getInt("DiscountID");
////                            if (discountID != 0) {
////                                st3.setInt(1, discountID);
////                                ResultSet discountResult = st3.executeQuery();
////                                discountResult.next();
////                                discountPercent = discountResult.getDouble(1);
////                                LocalDateTime dateStart = discountResult.getTimestamp(2).toLocalDateTime();
////                                LocalDateTime dateEnd = discountResult.getTimestamp(3).toLocalDateTime();
////                                LocalDateTime now = LocalDateTime.now();
////                                isOnSale = now.isAfter(dateStart) && now.isBefore(dateEnd) && discountPercent > 0;
////                            }
////
////                            double price = getPrice(id);
////
////                            meal = new Meal(id, name, description, price, isOnSale, discountID, discountPercent, content, category, imgURL, status);
////                        }
////                    }
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////            // Log the exception or handle it as needed
////        }
////
////        return meal;
////    }
//    

    public Meal getDayMealFromID(String id) {
        Meal meal = null;
        String getMealSql = "select [Id],[name],[content],[category],[imgURL],[status] from Meal\n"
                + "where Id = ?";
        try (Connection cn = JDBCUtil.getConnection();
                PreparedStatement pst = cn.prepareStatement(getMealSql);) {
            ResultSet table = pst.executeQuery();
            while(table.next()){
                
            }
        } catch (Exception e) {

        } finally {

        }
        return meal;
    }
}
