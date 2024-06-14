/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.order;

import Utility.JDBCUtil;
import dao.account.AddressDAO;
import dto.account.Address;
import dto.account.User;
import dto.order.Order;
import dto.order.OrderItem;
import dto.product.Product;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        Map<Integer, Order> orders = new LinkedHashMap<>();

        try (Connection conn = JDBCUtil.getConnection()) {
            // Getting connection from JDBCUtil


            String orderQuery = "SELECT orderId,orderDate,checkingDAte,abortDate,status,customerID"
                    + " FROM [Order] WHERE customerID = ?"
                    + " Order by orderDate desc";
            pstmt = conn.prepareStatement(orderQuery);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();
            if (rs != null) {
                AddressDAO addressDao = new AddressDAO();
                OrderItemDAO orderItemDAO = new OrderItemDAO();
                while (rs.next()) {
                        int orderId = rs.getInt("orderID");
                        List<OrderItem> listOfOrderItem = orderItemDAO.getOrderDetails(orderId);
                        
                        Address address = addressDao.getAddressByCustomerId(customerId);
                        Order order = new Order(
                                orderId,
                                rs.getTimestamp(2).toLocalDateTime(),
                                rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null,
                                rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null,
                                rs.getInt("status"),
                                customerId,address
                                
                                
                        );
                        order.setOrderDetail(listOfOrderItem);
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


    public int updateOrderStatus(int orderid, int newstatus) {
        String updateChecking = "update [Order] set checkingDate = ? where orderId=?";
        
        int rs = 0;
        Connection cn = null;
        try {
            //b1tao ket noi
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                //b2:viet query va exec query
                String sql = "update [Order] set status=? where orderId=?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setInt(1, newstatus);
                pst.setInt(2, orderid);
                rs = pst.executeUpdate();
                
                if (newstatus == 4){
                    LocalDateTime completeDay = LocalDateTime.now();
                    PreparedStatement updateDay = cn.prepareStatement(updateChecking);
                    updateDay.setTimestamp(1, Timestamp.valueOf(completeDay));
                    updateDay.setInt(2, orderid);
                    updateDay.executeUpdate();
                    
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
        return rs;
    }

    public Map<Integer,Order> getAllOrdersByStatus(int status) {
        Map<Integer,Order> list = new LinkedHashMap<>();
        Connection cn = null;
        try {
            //b1tao ket noi
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                //b2:viet query va exec query
                String sql = "select orderId,orderDate,checkingDate,abortDate,status,customerID\n"
                        + "from [Order]\n"
                        + "where Status=?\n"
                        + " Order by orderDate desc";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setInt(1, status);
                ResultSet rs = pst.executeQuery();
                if (rs != null) {
                    AddressDAO addressDao = new AddressDAO();
                    OrderItemDAO orderItemDAO = new OrderItemDAO();
                    
                    while (rs.next()) {
                        int orderId = rs.getInt("orderID");
                        int customerId = rs.getInt("customerID");
                        Address address = addressDao.getAddressByCustomerId(customerId);
                        List<OrderItem> listOfOrderItem = orderItemDAO.getOrderDetails(orderId);
                        Order order = new Order(
                                orderId,
                                rs.getTimestamp(2).toLocalDateTime(),
                                rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null,
                                rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null,
                                rs.getInt("status"),
                                customerId,address
                                
                                
                        );
                        order.setOrderDetail(listOfOrderItem);
                        list.put(orderId,order);
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
                + "where orderId=?"
                + " Order by orderDate desc";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    LocalDateTime orderDate = rs.getTimestamp(2).toLocalDateTime();
                    LocalDateTime checkingDate = rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null;
                    LocalDateTime abortDate = rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null;
                    int status = rs.getInt(5);
                    int customerID = rs.getInt(6);
                    AddressDAO dao = new AddressDAO();
                    OrderItemDAO orderItemDAO = new OrderItemDAO();
                    List<OrderItem> listOfOrderItem = orderItemDAO.getOrderDetails(id);
                    Address address = dao.getAddressByCustomerId(customerID);

                    Order order = new Order(id, orderDate, checkingDate, abortDate, status, customerID,address);
                    order.setOrderDetail(listOfOrderItem);
                    return order;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public void applyOrder(Map<Product,Integer> cart, User user) throws Exception {
        String orderItemInsert = "INSERT INTO orderItem(quantity,price,productID,orderID) values (?,?,?,?)";
        String orderInsert = "INSERT INTO [Order](orderDate,status,customerId) values(?,?,?)";
        Connection conn = JDBCUtil.getConnection();
        try (
                PreparedStatement orderStatement = conn.prepareStatement(orderInsert,Statement.RETURN_GENERATED_KEYS);
                PreparedStatement orderItemStatement = conn.prepareStatement(orderItemInsert)){
            LocalDateTime orderDate = LocalDateTime.now();
            
            conn.setAutoCommit(false);
            orderStatement.setTimestamp(1, Timestamp.valueOf(orderDate));
            orderStatement.setInt(2, 1);
            orderStatement.setInt(3, user.getId());
            
            orderStatement.executeUpdate();
            ResultSet orderIdRs = orderStatement.getGeneratedKeys();
            
            int orderId = 0;
            if (orderIdRs != null && orderIdRs.next()){
                orderId = orderIdRs.getInt(1);
                System.out.println(orderId);
            }else{
                throw new Exception("error with order insert");
            }
            
            for (Entry<Product,Integer> entry : cart.entrySet()){
                orderItemStatement.setInt(1, entry.getValue());
                if (entry.getKey().isOnSale()){
                    orderItemStatement.setDouble(2, entry.getKey().getPriceAfterDiscount());
                }else{
                    orderItemStatement.setDouble(2, entry.getKey().getPrice());
                }
                orderItemStatement.setString(3, entry.getKey().getId());
                orderItemStatement.setInt(4, orderId);
                orderItemStatement.addBatch();
            }
            
            int [] result = orderItemStatement.executeBatch();
            int updated = Arrays.stream(result).sum();
            if (updated != cart.size()){
                throw new Exception("error with item insert");
            }
            conn.commit();
        }catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            conn.rollback();
        }finally{
            conn.setAutoCommit(true);
            JDBCUtil.closeConnection(conn);
        }
        
    }   
    
    public void abortOrderByUser(int orderId) throws Exception{
        String updateAbortSql = "update [Order] set abortDate = ? where orderId=? ";
        String viewCurrentStatus = "select status from [Order] where orderId = ?";
        String updateStatusSql = "update [Order] set status=3 where orderId=?";
        Connection connection = JDBCUtil.getConnection();
        try(PreparedStatement pstUpdateDate = connection.prepareStatement(updateAbortSql);
                PreparedStatement pstView = connection.prepareStatement(viewCurrentStatus);
                PreparedStatement pstUpdateStatus = connection.prepareStatement(updateStatusSql)){
            
            pstView.setInt(1, orderId);
            ResultSet rs = pstView.executeQuery();
            if (rs != null && rs.next()){
                if (rs.getInt(1) == 2){
                    LocalDateTime abortDate = LocalDateTime.now();
                    pstUpdateDate.setTimestamp(1, Timestamp.valueOf(abortDate));
                    pstUpdateDate.setInt(2, orderId);
                    pstUpdateDate.executeUpdate();
                }
            }
            
            pstUpdateStatus.setInt(1, orderId);
            pstUpdateStatus.executeUpdate();
            
            
            
        }catch(Exception e){
            e.printStackTrace();
            connection.rollback();
            
        }finally{
            JDBCUtil.closeConnection(connection);
        }
    }


}
