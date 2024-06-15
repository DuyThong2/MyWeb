/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.order;

import Utility.JDBCUtil;
import dao.account.AddressDAO;
import dao.product.ProductDAO;
import dto.account.Address;
import dto.account.User;
import dto.order.Order;
import dto.order.OrderItem;
import dto.product.Product;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
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
                    List<OrderItem> listOfOrderItem = orderItemDAO.getOrderDetails(orderId,conn);

                    Address address = addressDao.getAddressByCustomerId(customerId,conn);
                    Order order = new Order(
                            orderId,
                            rs.getTimestamp(2).toLocalDateTime(),
                            rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null,
                            rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null,
                            rs.getInt("status"),
                            customerId, address
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

                if (newstatus == 4) {
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

    public Map<Integer, Order> getAllOrdersByStatus(int status) {
        Map<Integer, Order> orders = new LinkedHashMap<>();
        Connection cn = null;

        try {
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                // Query to get all orders by status
                String orderSql = "SELECT orderId, orderDate, checkingDate, abortDate, status, customerID "
                        + "FROM [Order] "
                        + "WHERE status = ? "
                        + "ORDER BY orderDate DESC";

                // Query to get order details for a specific order
                String orderItemSql = "SELECT orderItemID, quantity, price, productID, orderID "
                        + "FROM [OrderItem] "
                        + "WHERE orderID = ?";

                PreparedStatement orderPst = cn.prepareStatement(orderSql);
                orderPst.setInt(1, status);
                ResultSet orderRs = orderPst.executeQuery();

                AddressDAO addressDao = new AddressDAO();
                PreparedStatement orderItemPst = cn.prepareStatement(orderItemSql);

                while (orderRs.next()) {
                    int orderId = orderRs.getInt("orderId");
                    int customerId = orderRs.getInt("customerID");
                    LocalDateTime orderDate = orderRs.getTimestamp("orderDate").toLocalDateTime();
                    LocalDateTime checkingDate = orderRs.getTimestamp("checkingDate") != null ? orderRs.getTimestamp("checkingDate").toLocalDateTime() : null;
                    LocalDateTime abortDate = orderRs.getTimestamp("abortDate") != null ? orderRs.getTimestamp("abortDate").toLocalDateTime() : null;
                    int orderStatus = orderRs.getInt("status");

                    Address address = addressDao.getAddressByCustomerId(customerId,cn);

                    // Fetch order items for this order
                    orderItemPst.setInt(1, orderId);
                    ResultSet orderItemRs = orderItemPst.executeQuery();
                    List<OrderItem> orderItems = new ArrayList<>();

                    while (orderItemRs.next()) {
                        int orderItemId = orderItemRs.getInt("orderItemID");
                        int quantity = orderItemRs.getInt("quantity");
                        double price = orderItemRs.getDouble("price");
                        String productId = orderItemRs.getString("productID");

                        Product product = ProductDAO.getProductById(productId); // Assumes ProductDAO has a getProductById method
                        OrderItem orderItem = new OrderItem(orderItemId, quantity, price, product);
                        orderItems.add(orderItem);
                    }

                    // Create Order object and add to the map
                    Order order = new Order(orderId, orderDate, checkingDate, abortDate, orderStatus, customerId, address);
                    order.setOrderDetail(orderItems);
                    orders.put(orderId, order);
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

        return orders;
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
                    List<OrderItem> listOfOrderItem = orderItemDAO.getOrderDetails(id,conn);
                    Address address = dao.getAddressByCustomerId(customerID,conn);

                    Order order = new Order(id, orderDate, checkingDate, abortDate, status, customerID, address);
                    order.setOrderDetail(listOfOrderItem);
                    return order;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void applyOrder(Map<Product, Integer> cart, User user) throws Exception {
        String orderItemInsert = "INSERT INTO orderItem(quantity,price,productID,orderID) values (?,?,?,?)";
        String orderInsert = "INSERT INTO [Order](orderDate,status,customerId) values(?,?,?)";
        Connection conn = JDBCUtil.getConnection();
        try (
                PreparedStatement orderStatement = conn.prepareStatement(orderInsert, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement orderItemStatement = conn.prepareStatement(orderItemInsert)) {
            LocalDateTime orderDate = LocalDateTime.now();

            conn.setAutoCommit(false);
            orderStatement.setTimestamp(1, Timestamp.valueOf(orderDate));
            orderStatement.setInt(2, 1);
            orderStatement.setInt(3, user.getId());

            orderStatement.executeUpdate();
            ResultSet orderIdRs = orderStatement.getGeneratedKeys();

            int orderId = 0;
            if (orderIdRs != null && orderIdRs.next()) {
                orderId = orderIdRs.getInt(1);
                System.out.println(orderId);
            } else {
                throw new Exception("error with order insert");
            }

            for (Entry<Product, Integer> entry : cart.entrySet()) {
                orderItemStatement.setInt(1, entry.getValue());
                if (entry.getKey().isOnSale()) {
                    orderItemStatement.setDouble(2, entry.getKey().getPriceAfterDiscount());
                } else {
                    orderItemStatement.setDouble(2, entry.getKey().getPrice());
                }
                orderItemStatement.setString(3, entry.getKey().getId());
                orderItemStatement.setInt(4, orderId);
                orderItemStatement.addBatch();
            }

            int[] result = orderItemStatement.executeBatch();
            int updated = Arrays.stream(result).sum();
            if (updated != cart.size()) {
                throw new Exception("error with item insert");
            }
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            JDBCUtil.closeConnection(conn);
        }

    }

    public void abortOrderByUser(int orderId) throws Exception {
        String updateAbortSql = "update [Order] set abortDate = ? where orderId=? ";
        String viewCurrentStatus = "select status from [Order] where orderId = ?";
        String updateStatusSql = "update [Order] set status=3 where orderId=?";
        Connection connection = JDBCUtil.getConnection();
        try (PreparedStatement pstUpdateDate = connection.prepareStatement(updateAbortSql);
                PreparedStatement pstView = connection.prepareStatement(viewCurrentStatus);
                PreparedStatement pstUpdateStatus = connection.prepareStatement(updateStatusSql)) {

            pstView.setInt(1, orderId);
            ResultSet rs = pstView.executeQuery();
            if (rs != null && rs.next()) {
                if (rs.getInt(1) == 2) {
                    LocalDateTime abortDate = LocalDateTime.now();
                    pstUpdateDate.setTimestamp(1, Timestamp.valueOf(abortDate));
                    pstUpdateDate.setInt(2, orderId);
                    pstUpdateDate.executeUpdate();
                }
            }

            pstUpdateStatus.setInt(1, orderId);
            pstUpdateStatus.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();

        } finally {
            JDBCUtil.closeConnection(connection);
        }
    }

    public List<Order> getOrdersByOrderDate(LocalDateTime orderDate) {
        List<Order> orders = new ArrayList<>();
        LocalDate dateOnly = orderDate.toLocalDate(); // Extract the date part

        String sql = "SELECT orderID, orderDate, checkingDate, abortDate, status, customerID "
                + "FROM [Order] "
                + "WHERE CAST(orderDate AS DATE) = ?"; // Compare only the date part

        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setDate(1, Date.valueOf(dateOnly)); // Use Date.valueOf() to set the parameter

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt(1);
                LocalDateTime orderDateResult = rs.getTimestamp(2).toLocalDateTime();
                LocalDateTime checkingDate = rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null;
                LocalDateTime abortDate = rs.getTimestamp(4) != null ? rs.getTimestamp(4).toLocalDateTime() : null;
                int status = rs.getInt(5);
                int customerID = rs.getInt(6);

                AddressDAO dao = new AddressDAO();
                OrderItemDAO orderItemDAO = new OrderItemDAO();
                List<OrderItem> listOfOrderItem = orderItemDAO.getOrderDetails(orderId,conn);
                Address address = dao.getAddressByCustomerId(customerID,conn);

                Order order = new Order(orderId, orderDateResult, checkingDate, abortDate, status, customerID, address);
                order.setOrderDetail(listOfOrderItem);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public List<Order> getOrdersByPartialAddress(String address) {
        List<Order> orders = new ArrayList<>();

        // Concatenate street, ward, district, and city with spaces in between
        String sql = "SELECT o.orderID, o.orderDate, o.checkingDate, o.abortDate, o.status, o.customerID, "
                + "a.street, a.ward, a.district, a.city "
                + "FROM [Order] o "
                + "JOIN [Address] a ON o.customerID = a.id "
                + "WHERE CONCAT(a.street, ' ', a.ward, ' ', a.district, ' ', a.city) LIKE ?";

        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            String searchPattern = "%" + address.trim() + "%";
            pst.setString(1, searchPattern);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("orderID");
                LocalDateTime orderDate = rs.getTimestamp("orderDate").toLocalDateTime();
                LocalDateTime checkingDate = rs.getTimestamp("checkingDate") != null ? rs.getTimestamp("checkingDate").toLocalDateTime() : null;
                LocalDateTime abortDate = rs.getTimestamp("abortDate") != null ? rs.getTimestamp("abortDate").toLocalDateTime() : null;
                int status = rs.getInt("status");
                int customerID = rs.getInt("customerID");

                String street = rs.getString("street");
                String ward = rs.getString("ward");
                String district = rs.getString("district");
                String city = rs.getString("city");
                Address addressObj = new Address(city, district, ward, street, customerID);

                OrderItemDAO orderItemDAO = new OrderItemDAO();
                List<OrderItem> listOfOrderItem = orderItemDAO.getOrderDetails(orderId,conn);

                Order order = new Order(orderId, orderDate, checkingDate, abortDate, status, customerID, addressObj);
                order.setOrderDetail(listOfOrderItem);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

}
