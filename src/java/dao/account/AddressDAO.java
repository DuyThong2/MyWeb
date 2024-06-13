/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.account;

import Utility.JDBCUtil;
import dto.account.Address;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Admin
 */
public class AddressDAO {
    
    public Address getAddressByCustomerId(int id){
        String sql = " SELECT [id],[street],[ward] ,[district] ,[city] "
                + "FROM [PRJ301].[dbo].[Address]"
                + "Where id ="+id;
        
         try (Connection conn = JDBCUtil.getConnection();
                 Statement st = conn.createStatement()){
             ResultSet rs = st.executeQuery(sql);
             if (rs != null){
                 if (rs.next()){
                     Address address = new Address(rs.getString(5),rs.getString(4),
                             rs.getString(3),rs.getString(2),rs.getInt(1));
                     return address;
                 }
             }
             
         }catch(Exception e){
             e.printStackTrace();
         }
        return null;
    }
}
