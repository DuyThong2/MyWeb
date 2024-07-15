package dao.plan;

import Utility.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DayPlanDAO {

    public int insertDayPlanList(String mealPlanId) {
        int result = 0;
        String insertDayPlanSql = "insert into DayPlan([id],[dayInWeek],[status],[MealId],[MealPlanId]) values(?,?,?,null,?,null)";
        String getCurrentIdSql = "select top 1 id from DayPlan order by id desc";
        Connection cn = null;
        PreparedStatement insertPst = null;
        Statement getRst = null;
        ResultSet getTable = null;

        try {
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                cn.setAutoCommit(false);

                // Get the current max ID from the table
                getRst = cn.createStatement();
                getTable = getRst.executeQuery(getCurrentIdSql);
                int currentId = 0;
                if (getTable.next()) {
                    currentId = getTable.getInt("id");
                } else {
                    // If there's no current ID, start from 1
                    currentId = 0;
                }

                // Prepare the insert statement
                insertPst = cn.prepareStatement(insertDayPlanSql);
                for (int i = 0; i < 7; i++) {
                    currentId++;
                    insertPst.setInt(1, currentId);
                    insertPst.setInt(2, i);
                    insertPst.setInt(3, 0);
                    insertPst.setString(4, mealPlanId);

                    result = insertPst.executeUpdate();
                    if (result == 0) {
                        System.out.println("not valid");
                        cn.rollback();
                        return result;
                    }
                }
                cn.commit();
            }
        } catch (Exception e) {
            try {
                if (cn != null) {
                    cn.rollback();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (getTable != null) {
                    getTable.close();
                }
                if (getRst != null) {
                    getRst.close();
                }
                if (insertPst != null) {
                    insertPst.close();
                }
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

    public int updateMealIdOnDayPlan(String addMealId, int dayPlanId) {
        // Database connection and SQL update statement
        String sql = "UPDATE DayPlan SET MealId = ? WHERE id = ?";

        int rowsAffected = 0;

        try (Connection conn = JDBCUtil.getConnection(); // Implement your method to get a database connection
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters for the SQL query
            pstmt.setString(1, addMealId); // Set the MealId
            pstmt.setInt(2, dayPlanId);    // Set the id of the DayPlan to be updated

            // Execute the update
            rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(DayPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Return the number of rows affected
        return rowsAffected;
    }

    public int removeMealIdOnDayPlan(int dayPlanId) {
        // SQL update statement to set MealId to NULL
        String sql = "UPDATE DayPlan SET MealId = NULL WHERE id = ?";

        // Number of rows affected
        int rowsAffected = 0;

        // Try-with-resources statement to ensure the resources are closed
        try (Connection conn = JDBCUtil.getConnection(); // Implement your method to get a database connection
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameter for the SQL query
            pstmt.setInt(1, dayPlanId); // Set the id of the DayPlan to be updated

            // Execute the update
            rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(DayPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Return the number of rows affected
        return rowsAffected;
    }
}
