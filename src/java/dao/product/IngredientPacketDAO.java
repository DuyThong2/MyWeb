/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.product;

import Utility.JDBCUtil;
import dto.product.Ingredient;
import dto.product.IngredientPacket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Admin
 */
public class IngredientPacketDAO {

    public void insertPacket(IngredientPacket packet) throws Exception {
        String sql1 = "INSERT INTO Product (id, name, description, isOnSale, discountID) VALUES (?, ?, ?, ?, ?)";
        String sql2 = "INSERT INTO IngredientPacket (id, price, status, mealID) VALUES (?, ?, ?, ?)";
        String sql3 = "INSERT INTO PacketContains (IngredientID, IngredientPacketID, quantity) VALUES (?, ?, ?)";

        Connection con = JDBCUtil.getConnection();
        try (PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2);
                PreparedStatement st3 = con.prepareStatement(sql3)) {
            con.setAutoCommit(false);

            st1.setString(1, packet.getId());
            st1.setString(2, packet.getName());
            st1.setString(3, packet.getDescription());
            st1.setBoolean(4, packet.isOnSale());
            if (packet.isOnSale()) {
                st1.setInt(5, packet.getDiscountID());
            } else {
                st1.setNull(5, Types.INTEGER);
            }

            st2.setString(1, packet.getId());
            st2.setDouble(2, packet.getPrice());
            st2.setString(3, "active");
            st2.setString(4, String.format("M%s", packet.getId().substring(1)));

            Map<Ingredient, Integer> contains = packet.getContains();
            for (Entry<Ingredient, Integer> entry : contains.entrySet()) {
                st3.setInt(1, entry.getKey().getId());
                st3.setString(2, packet.getId());
                st3.setInt(3, entry.getValue());
                st3.addBatch();
            }

            int[] results = {
                st1.executeUpdate(),
                st2.executeUpdate(),};
            int rows = Arrays.stream(st3.executeBatch()).sum();
            if (rows < contains.size()) {
                throw new Exception();
            }

            for (int result : results) {
                if (result <= 0) {
                    throw new Exception();
                }
            }

            con.commit();
        } catch (Exception ex) {
            con.rollback();
            ex.printStackTrace();
            throw new RuntimeException();
        } finally {
            con.setAutoCommit(true);
            JDBCUtil.closeConnection(con);
        }
    }

    public HashMap<Ingredient, Integer> getAllIngredientsFromMealId(String packetId) throws Exception {
        String sql1 = "SELECT [IngredientID], [quantity] "
                + "FROM [PRJ301].[dbo].[PacketContains] "
                + "WHERE IngredientPacketID = ?";

        HashMap<Ingredient, Integer> ingredientsMap = new HashMap<>();

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sql1);) {
            IngredientDAO dao = new IngredientDAO();
            st1.setString(1, packetId);
            ResultSet rs1 = st1.executeQuery();

            while (rs1.next()) {
                int ingredientId = rs1.getInt("IngredientID");
                int quantity = rs1.getInt("quantity");
                Ingredient ingredient = dao.getIngredientFromId(ingredientId);
                ingredientsMap.put(ingredient, quantity);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error fetching ingredients from meal ID: " + packetId, ex);
        }

        return ingredientsMap;
    }

    public void updatePacketInfoForMeal(String packetId, Map<Ingredient, Integer> inserted) throws Exception {
        String deleteSql = "DELETE FROM [PRJ301].[dbo].[PacketContains] WHERE IngredientPacketID = ?";
        String insertSql = "INSERT INTO [PRJ301].[dbo].[PacketContains] (IngredientID, IngredientPacketID, quantity) VALUES (?, ?, ?)";
        String updatePriceSql = "UPDATE [PRJ301].[dbo].[IngredientPacket] SET price = ? WHERE id = ?";

        try (Connection con = JDBCUtil.getConnection()) {
            con.setAutoCommit(false);  // Start transaction

            try (PreparedStatement deleteStmt = con.prepareStatement(deleteSql);
                    PreparedStatement insertStmt = con.prepareStatement(insertSql);
                    PreparedStatement updatePriceStmt = con.prepareStatement(updatePriceSql)) {

                // Delete existing entries
                deleteStmt.setString(1, packetId);
                deleteStmt.executeUpdate();

                double totalPrice = 0.0;

                // Insert new entries
                for (Map.Entry<Ingredient, Integer> entry : inserted.entrySet()) {
                    insertStmt.setInt(1, entry.getKey().getId());
                    insertStmt.setString(2, packetId);
                    insertStmt.setInt(3, entry.getValue());
                    insertStmt.addBatch();

                    // Calculate total price
                    totalPrice += entry.getKey().getPrice() * entry.getValue();
                }

                insertStmt.executeBatch();

                // Update packet price
                updatePriceStmt.setDouble(1, totalPrice);
                updatePriceStmt.setString(2, packetId);
                updatePriceStmt.executeUpdate();

                con.commit();  // Commit transaction
            } catch (Exception ex) {
                con.rollback();  // Rollback transaction in case of error
                ex.printStackTrace();
                throw new Exception("Error updating packet info for meal: " + packetId, ex);
            }
        }
    }

    public IngredientPacket getIngredientPacketFromId(String packetID) {
        String packetSql = "SELECT id, price, status FROM [PRJ301].[dbo].[IngredientPacket] WHERE id = ?";
        String productSql = "SELECT [description], [isOnSale], [DiscountID],[name] FROM [PRJ301].[dbo].[Product] WHERE id = ?";
        String sql3 = "select valuePercent,dateApply,dateEnd from Discount where id = ?";
        IngredientPacket ingredientPacket = null;

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement packetStmt = con.prepareStatement(packetSql);
                PreparedStatement productStmt = con.prepareStatement(productSql);
                PreparedStatement st3 = con.prepareStatement(sql3)) {

            // Get packet details
            packetStmt.setString(1, packetID);
            try (ResultSet packetRs = packetStmt.executeQuery()) {
                if (packetRs.next()) {
                    String id = packetRs.getString("id");
                    double price = packetRs.getDouble("price");
                    String status = packetRs.getString("status");

                    double discountPercent = 0;
                    // Get product details
                    productStmt.setString(1, packetID);
                    try (ResultSet productRs = productStmt.executeQuery()) {
                        if (productRs.next()) {
                            String name = productRs.getString(4);
                            String description = productRs.getString("description");
                            boolean isOnSale = productRs.getBoolean("isOnSale");
                            int discountID = productRs.getInt("DiscountID");
                            if (discountID != 0) {
                                st3.setInt(1, discountID);
                                ResultSet discountResult = st3.executeQuery();
                                discountResult.next();
                                discountPercent = discountResult.getDouble(1);
                                LocalDateTime dateStart = discountResult.getTimestamp(2).toLocalDateTime();
                                LocalDateTime dateEnd = discountResult.getTimestamp(3).toLocalDateTime();
                                LocalDateTime now = LocalDateTime.now();
                                isOnSale = now.isAfter(dateStart) && now.isBefore(dateEnd) && discountPercent > 0;
                            }

                            // Create IngredientPacket object
                            ingredientPacket = new IngredientPacket(id, name, description, isOnSale, discountID, discountPercent, price, status);
                        }
                    }
                }
            }

            // If packet not found, return null
            if (ingredientPacket == null) {
                return null;
            }

            // Get ingredients and their quantities
            Map<Ingredient, Integer> contains = getAllIngredientsFromMealId(packetID);
            if (contains != null && !contains.isEmpty()) {
                ingredientPacket.setContains(contains);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ingredientPacket;
    }

}
