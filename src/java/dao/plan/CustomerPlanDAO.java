/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.plan;

import Utility.JDBCUtil;
import dao.product.MealDAO;
import dto.plan.CustomerDayPlan;
import dto.plan.CustomerPlan;
import dto.plan.MealPlan;
import dto.product.Meal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class CustomerPlanDAO {

    public int insertCustomerPlan(MealPlan mealPlan, int week, int customerId) {
        int result = 0;
        CustomerDayPlanDAO cdpdao = new CustomerDayPlanDAO();
        String insertSql = "INSERT INTO [dbo].[CustomerPlan] (id, name, type, imgURL, content, status, CustomerId, week) VALUES (?, ?, ?, ?, ?, 1, ?, ?)";
        String checkExistSql = "SELECT [week], [CustomerId] FROM [CustomerPlan] WHERE customerId = ? AND week = ? AND status = 1";
        String getMaxIdSql = "  SELECT Top 1 id FROM [CustomerPlan] ORDER BY ID DESC";
        Connection cn = null;
        PreparedStatement checkExistPst = null;
        PreparedStatement getMaxIdPst = null;
        PreparedStatement insertPst = null;
        ResultSet checkResult = null;
        ResultSet maxIdResult = null;

        try {
            cn = JDBCUtil.getConnection();
            if (cn != null) {
                cn.setAutoCommit(false);  // Start transaction

                checkExistPst = cn.prepareStatement(checkExistSql);
                checkExistPst.setInt(1, customerId);
                checkExistPst.setInt(2, week);
                checkResult = checkExistPst.executeQuery();

                if (!checkResult.next()) {
                    getMaxIdPst = cn.prepareStatement(getMaxIdSql);
                    maxIdResult = getMaxIdPst.executeQuery();
                    int newId = 1;
                    if (maxIdResult.next()) {
                        newId = maxIdResult.getInt("id") + 1;
                    }
                    insertPst = cn.prepareStatement(insertSql);
                    insertPst.setInt(1, newId);
                    insertPst.setString(2, mealPlan.getName().trim());
                    insertPst.setString(3, mealPlan.getType().trim());
                    insertPst.setString(4, mealPlan.getImgUrl().trim());
                    insertPst.setString(5, mealPlan.getContent().trim());
                    insertPst.setInt(6, customerId);
                    insertPst.setInt(7, week);

                    result = insertPst.executeUpdate();
                    if (result > 0) {
                        result = newId;
                        cn.commit();
                    } else {
                        System.out.println("ROLLBACK1");
                        cn.rollback();  // Rollback transaction if insertion of CustomerPlan fails
                    }
                } else {
                    System.out.println("ROLLBACK2");

                    cn.rollback();  // Rollback transaction if CustomerPlan already exists
                }
            }
        } catch (Exception e) {
            if (cn != null) {
                try {
                    System.out.println("ROLLBACK3");
                    cn.rollback();  // Rollback transaction on exception
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (checkResult != null) {
                    checkResult.close();
                }
                if (maxIdResult != null) {
                    maxIdResult.close();
                }
                if (checkExistPst != null) {
                    checkExistPst.close();
                }
                if (getMaxIdPst != null) {
                    getMaxIdPst.close();
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

    public TreeMap<Integer, CustomerPlan> getAllCustomerPlansById(int customerId) {
        TreeMap<Integer, CustomerPlan> customerPlans = new TreeMap<>();
        String GET_CUSTOMER_PLANS_QUERY
                = "SELECT id, name, type, content, imgURL, status, CustomerId, week, dateApply "
                + "FROM CustomerPlan WHERE CustomerId = ? and status =1";
        String GET_CUSTOMER_DAY_PLANS_QUERY
                = "SELECT id, customerPlanId, dayInWeek, status, MealId "
                + "FROM CustomerDayPlan WHERE customerPlanId = ?";

        MealDAO mdao = new MealDAO();
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement planStmt = con.prepareStatement(GET_CUSTOMER_PLANS_QUERY);
                PreparedStatement dayPlanStmt = con.prepareStatement(GET_CUSTOMER_DAY_PLANS_QUERY);) {

            // Fetch CustomerPlans
            planStmt.setInt(1, customerId);
            try (ResultSet planRs = planStmt.executeQuery()) {
                while (planRs.next()) {
                    int planId = planRs.getInt("id");
                    String name = planRs.getString("name");
                    String type = planRs.getString("type");
                    String content = planRs.getString("content");
                    String imgURL = planRs.getString("imgURL");
                    int status = planRs.getInt("status");
                    int week = planRs.getInt("week");
                    int getCustomerId = planRs.getInt("CustomerId");
                    Date dateApply = planRs.getDate("dateApply");

                    List<CustomerDayPlan> dayPlans = new ArrayList<>();

                    // Fetch associated CustomerDayPlans
                    dayPlanStmt.setInt(1, planId);
                    try (ResultSet dayPlanRs = dayPlanStmt.executeQuery()) {
                        while (dayPlanRs.next()) {
                            int dayPlanId = dayPlanRs.getInt("id");
                            int customerPlanId = dayPlanRs.getInt("customerPlanId");
                            int dayInWeek = dayPlanRs.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanRs.getInt("status");
                            String mealId = dayPlanRs.getString("MealId");

                            Meal meal = null;

                            // Fetch associated Meal
                            meal = mdao.getMealFromId(mealId);
                            CustomerDayPlan dayPlan = new CustomerDayPlan(dayPlanId, customerPlanId, dayInWeek, dayPlanStatus, meal);
                            dayPlans.add(dayPlan);
                        }
                    }

                    CustomerPlan customerPlan = new CustomerPlan(planId, name, type, content, imgURL, status, week, dayPlans, dateApply, getCustomerId);
                    customerPlans.put(week, customerPlan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return customerPlans;
    }

    public TreeMap<Integer, CustomerPlan> getAllCustomerPlanById(int customerId) {
        TreeMap<Integer, CustomerPlan> customerPlans = new TreeMap<>();
        String GET_CUSTOMER_PLANS_QUERY
                = "SELECT id, name, type, content, imgURL, status,CustomerId, week, dateApply "
                + "FROM CustomerPlan WHERE CustomerId = ? and status =1";
        String GET_CUSTOMER_DAY_PLANS_QUERY
                = "SELECT id, customerPlanId, dayInWeek, status, MealId "
                + "FROM CustomerDayPlan WHERE customerPlanId = ?";

        MealDAO mdao = new MealDAO();
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement planStmt = con.prepareStatement(GET_CUSTOMER_PLANS_QUERY);
                PreparedStatement dayPlanStmt = con.prepareStatement(GET_CUSTOMER_DAY_PLANS_QUERY);) {

            // Fetch CustomerPlans
            planStmt.setInt(1, customerId);
            try (ResultSet planRs = planStmt.executeQuery()) {
                while (planRs.next()) {
                    int planId = planRs.getInt("id");
                    String name = planRs.getString("name");
                    String type = planRs.getString("type");
                    String content = planRs.getString("content");
                    String imgURL = planRs.getString("imgURL");
                    int status = planRs.getInt("status");
                    int week = planRs.getInt("week");
                    int getCustomerId = planRs.getInt("CustomerId");
                    Date dateApply = planRs.getDate("dateApply");

                    List<CustomerDayPlan> dayPlans = new ArrayList<>();

                    // Fetch associated CustomerDayPlans
                    dayPlanStmt.setInt(1, planId);
                    try (ResultSet dayPlanRs = dayPlanStmt.executeQuery()) {
                        while (dayPlanRs.next()) {
                            int dayPlanId = dayPlanRs.getInt("id");
                            int customerPlanId = dayPlanRs.getInt("customerPlanId");
                            int dayInWeek = dayPlanRs.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanRs.getInt("status");
                            String mealId = dayPlanRs.getString("MealId");

                            Meal meal = null;

                            // Fetch associated Meal
                            meal = mdao.getMealFromId(mealId);

                            CustomerDayPlan dayPlan = new CustomerDayPlan(dayPlanId, customerPlanId, dayInWeek, dayPlanStatus, meal);
                            dayPlans.add(dayPlan);
                        }
                    }

                    CustomerPlan customerPlan = new CustomerPlan(planId, name, type, content, imgURL, status, week, dayPlans, dateApply, getCustomerId);
                    customerPlans.put(week, customerPlan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return customerPlans;
    }

    public CustomerPlan getCustomerPlanById(int customerPlanId) {
        CustomerPlan customerPlan = null;

        String GET_CUSTOMER_PLANS_QUERY
                = "SELECT id, name, type, content, imgURL, status,CustomerId, week, dateApply "
                + "FROM CustomerPlan WHERE id = ? and status =1";
        String GET_CUSTOMER_DAY_PLANS_QUERY
                = "SELECT id, customerPlanId, dayInWeek, status, MealId "
                + "FROM CustomerDayPlan WHERE customerPlanId = ?";

        MealDAO mdao = new MealDAO();
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement planStmt = con.prepareStatement(GET_CUSTOMER_PLANS_QUERY);
                PreparedStatement dayPlanStmt = con.prepareStatement(GET_CUSTOMER_DAY_PLANS_QUERY);) {

            // Fetch CustomerPlans
            planStmt.setInt(1, customerPlanId);
            try (ResultSet planRs = planStmt.executeQuery()) {
                while (planRs.next()) {
                    int planId = planRs.getInt("id");
                    String name = planRs.getString("name");
                    String type = planRs.getString("type");
                    String content = planRs.getString("content");
                    String imgURL = planRs.getString("imgURL");
                    int getCustomerId = planRs.getInt("CustomerId");
                    int status = planRs.getInt("status");
                    int week = planRs.getInt("week");
                    Date dateApply = planRs.getDate("dateApply");

                    List<CustomerDayPlan> dayPlans = new ArrayList<>();

                    // Fetch associated CustomerDayPlans
                    dayPlanStmt.setInt(1, planId);
                    try (ResultSet dayPlanRs = dayPlanStmt.executeQuery()) {
                        while (dayPlanRs.next()) {
                            int dayPlanId = dayPlanRs.getInt("id");
                            int getCustomerPlanId = dayPlanRs.getInt("customerPlanId");
                            int dayInWeek = dayPlanRs.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanRs.getInt("status");
                            String mealId = dayPlanRs.getString("MealId");

                            Meal meal = null;

                            // Fetch associated Meal
                            meal = mdao.getMealFromId(mealId);

                            CustomerDayPlan dayPlan = new CustomerDayPlan(dayPlanId, customerPlanId, dayInWeek, dayPlanStatus, meal);
                            dayPlans.add(dayPlan);
                        }
                    }
                    customerPlan = new CustomerPlan(planId, name, type, content, imgURL, status, week, dayPlans, dateApply,getCustomerId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return customerPlan;
    }

    public int deleteCustomerPlan(int deleteId) {
        int rowsAffected = 0;
        String DELETE_CUSTOMER_PLAN_QUERY = "UPDATE CustomerPlan SET status = 0 WHERE id = ?";

        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement deletePlanStmt = con.prepareStatement(DELETE_CUSTOMER_PLAN_QUERY)) {

            // Update CustomerPlan status
            deletePlanStmt.setInt(1, deleteId);
            rowsAffected = deletePlanStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("CustomerPlan with ID " + deleteId + " has been marked as deleted.");
            } else {
                System.out.println("No CustomerPlan found with ID " + deleteId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        } catch (Exception ex) {
            Logger.getLogger(CustomerPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rowsAffected;
    }

}
