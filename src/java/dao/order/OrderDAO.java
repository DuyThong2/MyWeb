/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.order;

import Utility.JDBCUtil;
import dto.order.Order;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class OrderDAO {

    public Map<Integer, Order> getOrdersByCustomerId(int customerId) {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<Integer, Order> orders = new HashMap<>();

        try (Connection conn = JDBCUtil.getConnection()) {
            // Getting connection from JDBCUtil


            String orderQuery = "SELECT orderId,orderDate,checkingDAte,abortDate,status,customerID"
                    + " FROM [Order] WHERE customerID = ?";
            pstmt = conn.prepareStatement(orderQuery);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("orderID"),
                            rs.getTimestamp(2).toLocalDateTime(),
                            rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null,
                            rs.getTimestamp(4) != null ? rs.getTimestamp(3).toLocalDateTime() : null,
                            rs.getString("status"),
                            rs.getInt("customerID")
                    );
                    orders.put(order.getOrderID(), order);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }



            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return orders;
    }


    public int updateOrderStatus(int orderid, String newstatus) {
        int rs = 0;
        Connection cn = null;
        try {
            //b1tao ket noi
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                //b2:viet query va exec query
                String sql = "update [Order] set status=? where orderId=?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, newstatus);
                pst.setInt(2, orderid);
                rs = pst.executeUpdate();
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
        return rs;
    }

    public ArrayList<Order> getAllOrdersByStatus(String status) {
        ArrayList<Order> list = new ArrayList<>();
        Connection cn = null;
        try {
            //b1tao ket noi
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                //b2:viet query va exec query
                String sql = "select orderId,orderDate,checkingDate,abortDate,status,customerID\n"
                        + "from [Order]\n"
                        + "where Status=?\n"
                        + "Order by orderDate desc";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, status);
                ResultSet rs = pst.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        Order order = new Order(
                                rs.getInt("orderID"),
                                rs.getTimestamp(2).toLocalDateTime(),
                                rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null,
                                rs.getTimestamp(4) != null ? rs.getTimestamp(3).toLocalDateTime() : null,
                                rs.getString("status"),
                                rs.getInt("customerID")
                        );
                        list.add(order);
                    }
                }
                return list;
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

        return null;
    }

    public Order getOrderByOrderId(int id) {
        String sql = "select orderId,orderDate,checkingDate,abortDate,status,customerID\n"
                + "from [Order]\n"
                + "where orderId=?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    LocalDateTime orderDate = rs.getTimestamp(2).toLocalDateTime();
                    LocalDateTime checkingDate = rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null;
                    LocalDateTime abortDate = rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null;
                    String status = rs.getString(5);
                    int customerID = rs.getInt(6);

                    Order order = new Order(id, orderDate, checkingDate, abortDate, status, customerID);
                    return order;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
