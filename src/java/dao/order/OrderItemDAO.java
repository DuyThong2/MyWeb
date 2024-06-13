/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.order;

import Utility.JDBCUtil;
import dao.product.ProductDAO;
import dto.order.Order;
import dto.order.OrderItem;

import dto.product.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class OrderItemDAO {

    public List<OrderItem> getOrderDetails(int orderid) {
        ArrayList<OrderItem> list = new ArrayList<>();
        Connection cn = null;
        try {
            //b1tao ket noi
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                //b2:viet query va exec query
                String sql = "SELECT [orderItemID],[quantity],[price],[productID],[orderID]"
                        + " FROM [PRJ301].[dbo].[OrderItem] where orderID = ?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setInt(1, orderid);
                ResultSet rs = pst.executeQuery();
                if (rs != null) {
                    while (rs.next()) {

                        int orderItemId = rs.getInt(1);
                        int quantity = rs.getInt(2);
                        double price = rs.getDouble(3);
                        String productID = rs.getString(4);
                        Product product = ProductDAO.getProductById(productID);
                        OrderItem detail = new OrderItem(orderItemId, quantity, price, product);
                        list.add(detail);
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

        return list;
    }

    public int sumQuantitiesByOrderId(Order order) {
        String sql = "SELECT SUM(quantity) AS total_quantity FROM OrderItem WHERE orderID = ?";
        int totalQuantity = 0;

        try (Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

           preparedStatement.setInt(1, order.getOrderID());
            ResultSet resultSetItem = preparedStatement.executeQuery();
            if (resultSetItem != null) {
                if (resultSetItem.next()) {
                    totalQuantity = resultSetItem.getInt("total_quantity");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalQuantity;
    }

    public double sumTotalPriceByOrderId(Order order) {
        String sql = "SELECT SUM(quantity * price) AS total_price FROM OrderItem WHERE orderID = ?";
        double totalPrice = 0.0;

        try (Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, order.getOrderID());
            ResultSet resultSetItem = preparedStatement.executeQuery();
            if (resultSetItem!= null) {
                if (resultSetItem.next()) {
                    totalPrice = resultSetItem.getDouble("total_price");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalPrice;
    }

}
