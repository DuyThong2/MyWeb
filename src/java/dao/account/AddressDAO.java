/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.account;

import Utility.JDBCUtil;
import dto.account.Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class AddressDAO {

    public Address getAddressByCustomerId(int id,Connection conn) {
        String sql = " SELECT [id],[street],[ward] ,[district] ,[city] "
                + "FROM [PRJ301].[dbo].[Address]"
                + "Where id =" + id;

        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            if (rs != null) {
                if (rs.next()) {
                    Address address = new Address(rs.getString(5), rs.getString(4),
                            rs.getString(3), rs.getString(2), rs.getInt(1));
                    if (address.getCity() == null){
                        return null;
                    }
                    return address;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAddressForUser(Address address) {

        String sql = "UPDATE [Address] SET city = ?, district = ?, ward = ?, street = ? WHERE id = ?";
        try (Connection cn = JDBCUtil.getConnection();
                PreparedStatement pst = cn.prepareStatement(sql);) {

           
            pst.setString(1, address.getCity());
            pst.setString(2, address.getDistrict());
            pst.setString(3, address.getWard());
            pst.setString(4, address.getStreet());
            pst.setInt(5, address.getCustomerId());

            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Integer> getCustomerIdsByPartialAddress(String address) {
        List<Integer> customerIds = new ArrayList<>();

        // Concatenate street, ward, district, and city with spaces in between
        String sql = "SELECT id FROM [Address] " +
                     "WHERE CONCAT(street, ' ', ward, ' ', district, ' ', city) LIKE ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            String searchPattern = "%" + address.trim() + "%";
            pst.setString(1, searchPattern);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                customerIds.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerIds;
    }
}
