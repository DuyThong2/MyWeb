/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.account;

import Utility.JDBCUtil;
import dto.account.Staff;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Admin
 */
public class StaffDAO {

    public Staff logInStaffByEmail(String email,String password) {
        Connection cn = null;
        Staff staff = null;
        String getStaffSql = null;
        Statement st = null;
        ResultSet rs =null;
        try {
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                getStaffSql = "select [email],[pw],[name] from [dbo].[Staff]\n where email='"+email+"'";
                st = cn.createStatement();
                rs = st.executeQuery(getStaffSql);
                while(rs.next()){
                    String comparedPassword = rs.getString("pw");
                    if(comparedPassword.equals(password)){
                        String name = rs.getString("name");
                        staff =new Staff(email,password,name);
                    }else{
                        staff = new Staff("NotFound");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return staff;
    }

}
