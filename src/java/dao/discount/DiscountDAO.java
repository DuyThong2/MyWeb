/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.discount;

import Utility.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class DiscountDAO {
    public double getDiscountPercentForProduct(int discountId) {
        String sql = "SELECT valuePercent FROM [Discount] WHERE id = ? AND dateEnd > ?";
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, discountId);
            st.setTimestamp(2, Timestamp.valueOf(currentDateTime));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 100; // Return 100 if no valid discount is found
    }
    
    
    public int getIdFromValues(double values){
        String sql1 = "select Id from [Discount] where valuePercent = ?";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sql1)) {
            st1.setDouble(1, values);
            ResultSet rs = st1.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public int insertNewDiscountValue(double value,int DurationDay){
        String sqlInsert = "insert into [Discount] (valuePercent,dateApply,dateEnd) values (?,?,?) ";
        
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sqlInsert)) {
            st1.setDouble(1, value);
            LocalDateTime dateStart = LocalDateTime.now();
            LocalDateTime dateEnd = dateStart.plusDays(DurationDay);
            
            st1.setTimestamp(2, Timestamp.valueOf(dateStart));
            st1.setTimestamp(3, Timestamp.valueOf(dateEnd));
            return st1.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    
    
}
