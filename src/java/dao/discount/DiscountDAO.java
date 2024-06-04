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

/**
 *
 * @author Admin
 */
public class DiscountDAO {
    public double getDiscountPercentForProduct(int discountId) {

        String sql1 = "select valuePercent from [Discount] where id = ?";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sql1)) {
            st1.setInt(1, discountId);
            ResultSet rs = st1.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 100;
    }
}
