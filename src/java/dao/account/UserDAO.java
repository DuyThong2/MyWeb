/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.account;

import Utility.JDBCUtil;
import dao.order.OrderDAO;
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
        String query = "SELECT [id], [email], [pw], [name], [address], [phone], [imgURL], [status] FROM [PRJ301].[dbo].[Customers]";
        List<User> users = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
                Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("pw");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String imgURL = resultSet.getString("imgURL");
                String status = resultSet.getString("status");
                User user = new User(id, email, phone, name, address, phone, imgURL, status);
                users.add(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public List<User> getUsersByCategory(String seachValue, String searchCategory) {
        String sql = String.format("SELECT [id], [email], [pw], [name], [address], [phone], [imgURL], [status]"
                + " FROM [PRJ301].[dbo].[Customers]"
                + "where %s like ? ", searchCategory);
        List<User> users = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "%" + seachValue + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("pw");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String imgURL = resultSet.getString("imgURL");
                String status = resultSet.getString("status");
                User user = new User(id, email, phone, name, address, phone, imgURL, status);
                users.add(user);
            }
        } catch (Exception ex) {
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

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("pw"),
                        rs.getString("name"),
                        rs.getString("address"),
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

            if (userRs.next()) {
                user = new User(
                        userRs.getInt("id"),
                        userRs.getString("email"),
                        userRs.getString("pw"),
                        userRs.getString("name"),
                        userRs.getString("address"),
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

        String sql = "UPDATE Customers SET name=?, email=?, phone=?, address=?, imgURL=?, status=? WHERE id=?";

        try {
            conn = JDBCUtil.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPhone());
                ps.setString(4, user.getAddress());
                ps.setString(5, user.getImgURL());
                ps.setString(6, user.getStatus());
                ps.setInt(7, user.getId());

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

    public User getUserByEmail(String email, String password) {
        User returnUser = null;
        Connection cn = null;
        try {
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                String sql = "Select id,email,pw,name,address,phone,imgURL,status \n"
                        + "from Customers \n"
                        + "where email = ?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, email);
                ResultSet table = pst.executeQuery();
                while (table != null && table.next()) {
                    String testpassword = table.getString("pw");
                    if (password.equals(testpassword)) {
                        int id = table.getInt("id");
                        String name = table.getString("name");
                        String address = table.getString("address");
                        String phone = table.getString("phone");
                        String imgURL = table.getString("imgURL");
                        String status = table.getString("status");
                        returnUser = new User(id, email, password, name, address, phone, imgURL, status);
                    } else {
                        returnUser = new User();
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
        return returnUser;
    }

    public int registerNewAccount(User user) {
        int result = 0;
        Connection cn = null;
        try {
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                cn.setAutoCommit(false);
                //check if the email have already been signed up
                String checkSignSql = "select * from Customers where email=?";
                PreparedStatement pstCheckSign = cn.prepareStatement(checkSignSql);
                pstCheckSign.setString(1, user.getEmail());
                ResultSet checkSignTable = pstCheckSign.executeQuery();
                if (checkSignTable.next()) {
                    result = -2;// email have been signed up
                    cn.setAutoCommit(true);
                    return result;
                }
                
                String insertCustomerSql = "insert Customers(email,pw,name,phone,status) values(?,?,?,?,?)";
                PreparedStatement pstInsertCustomer= cn.prepareStatement(insertCustomerSql);
                pstInsertCustomer.setString(1, user.getEmail());
                pstInsertCustomer.setString(2, user.getPw());
                pstInsertCustomer.setString(3, user.getName());
                pstInsertCustomer.setString(4, user.getPhone());
                pstInsertCustomer.setString(5, user.getStatus());
                result = pstInsertCustomer.executeUpdate();
                if (result >= 1) {
                    String retrieveIdSql = "select top 1 id from Customers order by id desc";
                    PreparedStatement pstRetrieveId = cn.prepareStatement(retrieveIdSql);
                    ResultSet table = pstRetrieveId.executeQuery();
                    if (table.next()) {
                        String insertAddressSql = "insert Address values(?,?,?,?,?)";
                        PreparedStatement pstInsertAddress = cn.prepareStatement(insertAddressSql);
                        int id = table.getInt("id");
                        pstInsertAddress.setInt(1, id);
                        pstInsertAddress.setString(2, null);
                        pstInsertAddress.setString(3, null);
                        pstInsertAddress.setString(4, null);
                        pstInsertAddress.setString(5, null);
                        result = pstInsertAddress.executeUpdate();
                    }
                }
                cn.commit();

            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cn != null) {
                try {
                    // Rollback transaction on error
                    cn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            try {
                if (cn != null) {
                    cn.setAutoCommit(true);
                    cn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
