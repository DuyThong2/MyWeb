/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.product;

import dto.product.Ingredient;
import dto.product.Meal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Utility.JDBCUtil;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class IngredientDAO {

    public List<Ingredient> getAllIngredients() {
        String sql1 = "select IngredientId,name,unit,imgURL from Ingredient";
        String sql2 = "select top 1 value from IngredientPrice where priceID = ? and status ='active'";
        List<Ingredient> listOfIngredient = new ArrayList<>();

        try (Connection con = JDBCUtil.getConnection();
                Statement statement1 = con.createStatement();
                PreparedStatement st2 = con.prepareStatement(sql2)) {
            ResultSet rs = statement1.executeQuery(sql1);
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    st2.setInt(1, id);
                    ResultSet priceQuery = st2.executeQuery();
                    priceQuery.next();
                    double price = priceQuery.getDouble(1);
                    String name = rs.getString(2);
                    String unit = rs.getString(3);
                    String URL = rs.getString(4);
                    Ingredient ingredient = new Ingredient(id, name, unit, URL, price);
                    listOfIngredient.add(ingredient);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listOfIngredient;
    }

    public void InsertToTable(Ingredient ingredient) throws Exception {

        String sql1 = "insert into Ingredient (IngredientId,name,unit,imgURL) values (?,?,?,?) ";
        
        String sql3 = "insert into IngredientPrice (priceID,value,status,dateApply) values (?,?,?,?)";
        Connection con = JDBCUtil.getConnection();
        try (PreparedStatement st1 = con.prepareStatement(sql1);
                
                PreparedStatement st3 = con.prepareStatement(sql3);) {
            con.setAutoCommit(false);
            st1.setInt(1, ingredient.getId());
            st1.setString(2, ingredient.getName());
            st1.setString(3, ingredient.getUnit());
            st1.setString(4, ingredient.getImgURL());

            

            LocalDateTime dtn = LocalDateTime.now();

            st3.setInt(1, ingredient.getId());
            st3.setDouble(2, ingredient.getPrice());
            st3.setString(3, "active");
            st3.setTimestamp(4, Timestamp.valueOf(dtn));

            int[] results = {
                st1.executeUpdate(),
                st3.executeUpdate()
            };
            for (int result : results) {
                if (result <= 0) {
                    System.out.println("result");
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

    public List<Ingredient> getIngredientsByName(String name) {
        List<Ingredient> listOfIngredient = new ArrayList<>();
        String sql1 = "select IngredientId,name,unit,imgURL from Ingredient where name like ?";
        String sql2 = "select top 1 value from IngredientPrice where priceID = ? and status ='active'";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement statement1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2)) {

            statement1.setString(1, "%" + name.trim() + "%");
            ResultSet rs = statement1.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    st2.setInt(1, id);
                    ResultSet priceQuery = st2.executeQuery();
                    priceQuery.next();
                    double price = priceQuery.getDouble(1);
                    String name1 = rs.getString(2);
                    String unit = rs.getString(3);
                    String URL = rs.getString(4);
                    Ingredient ingredient = new Ingredient(id, name1, unit, URL, price);
                    listOfIngredient.add(ingredient);
                }

            }
            System.out.println(listOfIngredient.size());
            return listOfIngredient;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public Ingredient getIngredientByName(String name) {

        String sql1 = "select IngredientId,name,unit,imgURL from Ingredient where name = ?";
        String sql2 = "select top 1 value from IngredientPrice where priceID = ? and status ='active'";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement statement1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2)) {

            statement1.setString(1, name.trim());
            ResultSet rs = statement1.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    st2.setInt(1, id);
                    ResultSet priceQuery = st2.executeQuery();
                    priceQuery.next();
                    double price = priceQuery.getDouble(1);
                    String name1 = rs.getString(2);
                    String unit = rs.getString(3);
                    String URL = rs.getString(4);
                    return new Ingredient(id, name1, unit, URL, price);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }
    
    public void updateIngredient(Ingredient ingredient) throws Exception{
         String sql1 = "update Ingredient set name = ?,unit = ?,imgURL = ? where IngredientId = ?";
         String sql2 = "update IngredientPrice set status = 'disable' where priceID = ?";
         String sql3 = "insert into IngredientPrice (priceID,value,status,dateApply) values (?,?,?,?)";
         Connection con = JDBCUtil.getConnection();
         try (PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3);) {
            con.setAutoCommit(false);
            
            st1.setString(1, ingredient.getName());
            st1.setString(2, ingredient.getUnit());
            st1.setString(3, ingredient.getImgURL());
            st1.setInt(4, ingredient.getId());
            
            st2.setInt(1,ingredient.getId());
            

            LocalDateTime dtn = LocalDateTime.now();

            st3.setInt(1, ingredient.getId());
            st3.setDouble(2, ingredient.getPrice());
            st3.setString(3, "active");
            st3.setTimestamp(4, Timestamp.valueOf(dtn));

            int[] results = {
                st1.executeUpdate(),
                st2.executeUpdate(),
                st3.executeUpdate()
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
    
    public Ingredient getIngredientFromId(int id){
        String sql1 = "select IngredientId,name,unit,imgURL from Ingredient where IngredientId = ?";
        String sql2 = "select top 1 value from IngredientPrice where priceID = ? and status ='active'";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement statement1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2)
                ) {

            statement1.setInt(1, id);
            ResultSet rs = statement1.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int id1 = rs.getInt(1);
                    st2.setInt(1, id);
                    ResultSet priceQuery = st2.executeQuery();
                    priceQuery.next();
                    double price = priceQuery.getDouble(1);
                    String name = rs.getString(2);
                    String unit = rs.getString(3);
                    String URL = rs.getString(4);
                    return new Ingredient(id, name, unit, URL, price);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
