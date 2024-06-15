/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.account;

import Utility.JDBCUtil;
import dao.order.OrderDAO;
import dto.account.Address;
import dto.account.User;
import dto.order.Order;
import dto.product.Meal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class UserDAO {

    public List<User> getAllUser() {
    String userQuery = "SELECT [id], [email], [pw], [name], [phone], [imgURL], [status] FROM [PRJ301].[dbo].[Customers]";
    String addressQuery = "SELECT [street], [ward], [district], [city] FROM [PRJ301].[dbo].[Address] WHERE id = ?";
    List<User> users = new ArrayList<>();

    try (Connection conn = JDBCUtil.getConnection();
         Statement statement = conn.createStatement();
         PreparedStatement pst = conn.prepareStatement(addressQuery)) {

        ResultSet userResultSet = statement.executeQuery(userQuery);
        while (userResultSet.next()) {
            int id = userResultSet.getInt("id");
            String email = userResultSet.getString("email");
            String password = userResultSet.getString("pw");
            String name = userResultSet.getString("name");
            String phone = userResultSet.getString("phone");
            String imgURL = userResultSet.getString("imgURL");
            String status = userResultSet.getString("status");

            // Fetch the address for the current user
            pst.setInt(1, id);
            ResultSet addressResultSet = pst.executeQuery();
            Address address = null;
            if (addressResultSet.next()) {
                String street = addressResultSet.getString(1);
                String ward = addressResultSet.getString(2);
                String district = addressResultSet.getString(3);
                String city = addressResultSet.getString(4);
                address = new Address(city, district, ward, street, id) ;
            }

            // Create an address string
            

            // Create User object
            User user = new User(id, email, password, name, address, phone, imgURL, status);
            users.add(user);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return users;
}

    

    

    public List<User> getUsersByCategory(String seachValue, String searchCategory) {
        String sql =String.format("SELECT [id], [email], [pw], [name], [phone], [imgURL], [status]"
               + " FROM [PRJ301].[dbo].[Customers]"
               + "where %s like ? ",searchCategory);
        List<User> users = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, "%"+seachValue+"%");
            ResultSet resultSet = statement.executeQuery();
            AddressDAO addressDAO = new AddressDAO();
            while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Address address = addressDAO.getAddressByCustomerId(id,conn);
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("pw");
                    String name = resultSet.getString("name");
                    
                    String phone = resultSet.getString("phone");
                    String imgURL = resultSet.getString("imgURL");
                    String status = resultSet.getString("status");
                    User user = new User(id, email, phone, name, address, phone, imgURL, status);
                    users.add(user);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
       
       return users;
    }
    
    
    public User getUserWithOrders(int userId) {
        

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try (Connection conn = JDBCUtil.getConnection()) {

            

            // Get user details
            String userQuery = "SELECT * FROM Customers WHERE id = ?";
            pstmt = conn.prepareStatement(userQuery);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            AddressDAO addressDAO = new AddressDAO();
            Address address = addressDAO.getAddressByCustomerId(userId,conn);
            if (rs.next()) {
                user = new User(

                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("pw"),
                    rs.getString("name"),
                    address,
                    rs.getString("phone"),
                    rs.getString("imgURL"),
                    rs.getString("status")

                );
            }

            // Get user's orders
            if (user != null) {
                OrderDAO orderDAO = new OrderDAO();
                Map<Integer, Order> orders = orderDAO.getOrdersByCustomerId(userId);
                user.setOrderHistory(orders);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void updateCustomerStatus(int id, String status) {
        String sql = "UPDATE Customers SET status = ? WHERE id = ?";

        try (Connection conn = JDBCUtil.getConnection();

                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            


            pstmt.setString(1, status);
            pstmt.setInt(2, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Status updated successfully!");
            } else {
                System.out.println("No customer found with the provided email.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public User getUserById(int userId) {
        User user = null;
        String userQuery = "SELECT * FROM Customers WHERE id = ?";

        try (Connection connection = JDBCUtil.getConnection();
                PreparedStatement userStmt = connection.prepareStatement(userQuery);) {

            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();
            AddressDAO addressDAO = new AddressDAO();
            Address address = addressDAO.getAddressByCustomerId(userId,connection);
            if (userRs.next()) {
                user = new User(
                        userRs.getInt("id"),
                        userRs.getString("email"),
                        userRs.getString("pw"),
                        userRs.getString("name"),
                        address,
                        userRs.getString("phone"),
                        userRs.getString("imgURL"),
                        userRs.getString("status")
                );
            }

            if (user != null) {
                OrderDAO orderDAO = new OrderDAO();
                Map<Integer, Order> orders = orderDAO.getOrdersByCustomerId(userId);
                user.setOrderHistory(orders);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(user);
        return user;
    }
    
    public boolean updateUser(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean isUpdated = false;

        String sql = "UPDATE Customers SET name=?, email=?, phone=?,imgURL=?, status=? WHERE id=?";

        try {
            conn = JDBCUtil.getConnection();
            AddressDAO addressDAO = new AddressDAO();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPhone());
                addressDAO.updateAddressForUser(user.getAddress());
                ps.setString(4, user.getImgURL());
                ps.setString(5, user.getStatus());
                ps.setInt(6, user.getId());

                isUpdated = ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error updating user information: " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return isUpdated;
    }


}
