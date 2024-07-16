/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.plan;

import Utility.JDBCUtil;
import dao.product.MealDAO;
import dto.plan.CustomerDayPlan;
import dto.plan.DayPlan;
import dto.plan.MealPlan;
import dto.product.Meal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class CustomerDayPlanDAO {

    public int insertCustomerPlanList(int customerPlanId) {
        int result = 0;
        String insertCustomerPlanSql = "INSERT INTO CustomerDayPlan (id, customerPlanId, dayInWeek, status, mealId) VALUES (?, ?, ?, ?, ?)";
        String getCurrentIdSql = "SELECT TOP 1 id FROM CustomerDayPlan ORDER BY id DESC";
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
                    currentId = 1;
                }

                // Prepare the insert statement
                insertPst = cn.prepareStatement(insertCustomerPlanSql);
                for (int i = 0; i < 7; i++) {
                    currentId++;
                    insertPst.setInt(1, currentId);
                    insertPst.setInt(2, customerPlanId);
                    insertPst.setInt(3, i);
                    insertPst.setInt(4, 1);
                    insertPst.setString(5, null); // Assuming mealId is initially null

                    result = insertPst.executeUpdate();
                    if (result == 0) {
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

    public int insertCustomerPlanListWithMealPlan(int customerPlanId, MealPlan mealPlan) {
        int result = 0;
        String insertCustomerPlanSql = "INSERT INTO CustomerDayPlan (id, customerPlanId, dayInWeek, status, mealId) VALUES (?, ?, ?, ?, ?)";
        String getCurrentIdSql = "SELECT TOP 1 id FROM CustomerDayPlan ORDER BY id DESC";
        String getMealStatusSql = "SELECT status FROM Meal WHERE Id = ?";
        Connection cn = null;
        PreparedStatement insertPst = null;
        PreparedStatement mealStatusPst = null;
        Statement getRst = null;
        ResultSet getTable = null;
        ResultSet mealStatusRs = null;

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
                    currentId = 1;
                }

                // Prepare the insert statement
                insertPst = cn.prepareStatement(insertCustomerPlanSql);
                mealStatusPst = cn.prepareStatement(getMealStatusSql);

                for (int i = 0; i < 7; i++) {
                    System.out.println(i);
                    currentId++;
                    DayPlan dayplan = null;
                    String mealId = null;
                    if (!mealPlan.getDayPlanContains().isEmpty()) {
                        dayplan = mealPlan.getDayPlanContains().get(i);
                    }
                    if (dayplan != null) {
                        mealId = dayplan.getMealId();
                        if (mealId != null) {
                            // Check meal status
                            mealStatusPst.setString(1, mealId);
                            mealStatusRs = mealStatusPst.executeQuery();
                            if (mealStatusRs.next()) {
                                String mealStatus = mealStatusRs.getString("status");
                                if ("disable".equals(mealStatus)) {
                                    mealId = null; // Set mealId to null if status is 'disable'
                                }
                            }
                        }
                    }
                    insertPst.setInt(1, currentId);
                    insertPst.setInt(2, customerPlanId);
                    insertPst.setInt(3, i);
                    insertPst.setInt(4, 1);
                    insertPst.setString(5, mealId); // Assuming mealId is initially null
                    result = insertPst.executeUpdate();
                    if (result == 0) {
                        System.out.println("INNER ROLLBACK");
                        cn.rollback();
                        return result;
                    }
                }
                System.out.println("THONGBAKA 2");
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
                if (mealStatusRs != null) {
                    mealStatusRs.close();
                }
                if (getTable != null) {
                    getTable.close();
                }
                if (getRst != null) {
                    getRst.close();
                }
                if (mealStatusPst != null) {
                    mealStatusPst.close();
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

    public int insertCustomerDayPlan(CustomerDayPlan customerDayPlan) {
        int result = 0;
        String insertSql = "INSERT INTO [dbo].[CustomerDayPlan] (id, customerPlanId, dayInWeek, status, mealId) VALUES (?, ?, ?, ?, ?)";
        String getMaxIdSql = "SELECT MAX(id) AS maxId FROM [CustomerDayPlan]";
        Connection cn = null;

        try {
            cn = JDBCUtil.getConnection();
            cn.setAutoCommit(false);
            if (cn != null) {
                PreparedStatement getMaxIdPst = cn.prepareStatement(getMaxIdSql);
                ResultSet maxIdResult = getMaxIdPst.executeQuery();
                int newId = 1;
                if (maxIdResult.next()) {
                    newId = maxIdResult.getInt("maxId") + 1;
                }
                PreparedStatement pst = cn.prepareStatement(insertSql);
                pst.setInt(1, newId);
                pst.setInt(2, customerDayPlan.getCustomerPlanId());
                pst.setInt(3, customerDayPlan.getDayInWeek());
                pst.setInt(4, customerDayPlan.getStatus());
                pst.setString(5, customerDayPlan.getMeal().getId());
                result = pst.executeUpdate();
                if (result > 0) {
                    cn.commit();
                } else {
                    cn.rollback();
                }
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

    public int updateMealIdOnCustomerDayPlan(int mealId, int dayPlanId) {
        String sql = "UPDATE CustomerDayPlan SET mealId = ? WHERE id = ?";

        int rowsAffected = 0;

        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, mealId);
            pstmt.setInt(2, dayPlanId);

            rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(CustomerDayPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rowsAffected;
    }

    // Method to remove a mealId from a CustomerDayPlan
    public int removeMealIdOnCustomerDayPlan(int dayPlanId) {
        String sql = "UPDATE CustomerDayPlan SET mealId = NULL WHERE id = ?";

        int rowsAffected = 0;

        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, dayPlanId);

            rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(CustomerDayPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rowsAffected;
    }

    // Method to get a CustomerDayPlan by ID
    public CustomerDayPlan getCustomerDayPlanById(int id) {
        String sql = "SELECT * FROM CustomerDayPlan WHERE id = ?";
        CustomerDayPlan customerDayPlan = null;
        MealDAO mdao = new MealDAO();
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int customerPlanId = rs.getInt("customerPlanId");
                int dayInWeek = rs.getInt("dayInWeek");
                int status = rs.getInt("status");
                String mealId = rs.getString("mealId");
                Meal meal = mdao.getMealFromId(mealId);
                // Assuming you have a method to get a Meal by ID
                customerDayPlan = new CustomerDayPlan(id, customerPlanId, dayInWeek, status, meal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(CustomerDayPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return customerDayPlan;
    }

    public int addMealPlanToDayPlan(String mealId, int addDay, int customerPlanId) {
        int result = 0;
        try (Connection cn = JDBCUtil.getConnection();) {

            String sql = "  update CustomerDayPlan set MealId=? where CustomerPlanId = ? and dayInWeek=?  and MealId is null";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, mealId);
            pst.setInt(2, customerPlanId);
            pst.setInt(3, addDay);

            result = pst.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(CustomerDayPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public int deleteMealPlanfromDayPlan(int addDay, int customerPlanId) {
        int result = 0;
        try (Connection cn = JDBCUtil.getConnection();) {
            String sql = "  update CustomerDayPlan set MealId=null where CustomerPlanId = ? and dayInWeek=?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, customerPlanId);
            pst.setInt(2, addDay);
            result = pst.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(CustomerDayPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
}
