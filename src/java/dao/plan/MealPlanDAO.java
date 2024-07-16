/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.plan;

import Utility.JDBCUtil;
import dto.plan.DayPlan;
import dto.plan.MealPlan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class MealPlanDAO {

    public ArrayList<MealPlan> getAllMealPlans() {
        ArrayList<MealPlan> list = new ArrayList<>();
        String getAllMealPlanSql = "SELECT [id],[name],[type],[imgURL],[content],[status] from [dbo].[MealPlan] order by status desc";
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId] from [dbo].[DayPlan]\n"
                + "               where MealPlanId = ? order by dayInWeek Asc";
        try (Connection conn = JDBCUtil.getConnection();
                Statement statement = conn.createStatement();
                PreparedStatement pst = conn.prepareStatement(getDayPlanSql);) {

            ResultSet mealPlanRst = statement.executeQuery(getAllMealPlanSql);
            if (mealPlanRst != null) {
                while (mealPlanRst.next()) {
                    String mealPlanId = mealPlanRst.getString("id");
                    String name = mealPlanRst.getString("name");
                    String type = mealPlanRst.getString("type");
                    String imgURL = mealPlanRst.getString("imgURL");
                    String content = mealPlanRst.getString("content");
                    int mealPlanStatus = mealPlanRst.getInt("status");
                    List<DayPlan> dayPlanList = new ArrayList<>();
                    pst.setString(1, mealPlanId);
                    ResultSet dayPlanRst = pst.executeQuery();
                    if (dayPlanRst != null) {
                        while (dayPlanRst.next()) {
                            int dayPlanId = dayPlanRst.getInt("id");
                            int dayInWeek = dayPlanRst.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanRst.getInt("status");
                            String mealId = dayPlanRst.getString("MealId");
                            // String id, String mealId, String mealPlanId, String customerPlanId, int dayInWeek, int status
                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanId, -1, dayInWeek, dayPlanStatus);
                            dayPlanList.add(dayPlan);
                        }
                    }
                    // public MealPlan(String id, String name, String type, String content, String imgUrl, int status, List<DayPlan> dayPlanContains)
                    MealPlan mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, mealPlanStatus, dayPlanList);
                    list.add(mealPlan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<MealPlan> getAllMeanPlanByName(String searchName) {
        ArrayList<MealPlan> list = new ArrayList<>();
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId] from [dbo].[DayPlan]\n"
                + "where MealPlanId = ?  order by dayInWeek Asc";
        String getMealPlanByNameSql = "select [id],[type],[imgURL],[content],[status],[name] from [dbo].[MealPlan]\n"
                + "  where [name] like ? order by status desc";
        try (Connection cn = JDBCUtil.getConnection();
                PreparedStatement mealPlanPst = cn.prepareStatement(getMealPlanByNameSql);
                PreparedStatement dayPlanPst = cn.prepareStatement(getDayPlanSql);) {
            mealPlanPst.setString(1, "%" + searchName + "%");
            ResultSet mealPlanTable = mealPlanPst.executeQuery();
            if (mealPlanTable != null) {
                while (mealPlanTable.next()) {
                    String mealPlanId = mealPlanTable.getString("id");
                    String name = mealPlanTable.getString("name");
                    String type = mealPlanTable.getString("type");
                    String imgURL = mealPlanTable.getString("imgURL");
                    String content = mealPlanTable.getString("content");
                    int mealPlanStatus = mealPlanTable.getInt("status");
                    List<DayPlan> dayPlanList = new ArrayList<>();
                    dayPlanPst.setString(1, mealPlanId);
                    ResultSet dayPlanTable = dayPlanPst.executeQuery();
                    if (dayPlanTable != null) {
                        while (dayPlanTable.next()) {
                            int dayPlanId = dayPlanTable.getInt("id");
                            int dayInWeek = dayPlanTable.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanTable.getInt("status");
                            String mealId = dayPlanTable.getString("MealId");
                            // String id, String mealId, String mealPlanId, String customerPlanId, int dayInWeek, int status
                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanId, -1, dayInWeek, dayPlanStatus);
                            dayPlanList.add(dayPlan);
                        }
                    }
                    MealPlan mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, mealPlanStatus, dayPlanList);
                    list.add(mealPlan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MealPlan> getCustomerAllMeanPlanByName(String searchName) {
        ArrayList<MealPlan> list = new ArrayList<>();
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId] from [dbo].[DayPlan]\n"
                + "where MealPlanId = ? and status=1 order by dayInWeek Asc";
        String getMealPlanByNameSql = "select [id],[type],[imgURL],[content],[status],[name] from [dbo].[MealPlan]\n"
                + "  where [name] like ? and status=1";
        try (Connection cn = JDBCUtil.getConnection();
                PreparedStatement mealPlanPst = cn.prepareStatement(getMealPlanByNameSql);
                PreparedStatement dayPlanPst = cn.prepareStatement(getDayPlanSql);) {
            mealPlanPst.setString(1, "%" + searchName + "%");
            ResultSet mealPlanTable = mealPlanPst.executeQuery();
            if (mealPlanTable != null) {
                while (mealPlanTable.next()) {
                    String mealPlanId = mealPlanTable.getString("id");
                    String name = mealPlanTable.getString("name");
                    String type = mealPlanTable.getString("type");
                    String imgURL = mealPlanTable.getString("imgURL");
                    String content = mealPlanTable.getString("content");
                    int mealPlanStatus = mealPlanTable.getInt("status");
                    List<DayPlan> dayPlanList = new ArrayList<>();
                    dayPlanPst.setString(1, mealPlanId);
                    ResultSet dayPlanTable = dayPlanPst.executeQuery();
                    if (dayPlanTable != null) {
                        while (dayPlanTable.next()) {
                            int dayPlanId = dayPlanTable.getInt("id");
                            int dayInWeek = dayPlanTable.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanTable.getInt("status");
                            String mealId = dayPlanTable.getString("MealId");
                            // String id, String mealId, String mealPlanId, String customerPlanId, int dayInWeek, int status
                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanId, -1, dayInWeek, dayPlanStatus);
                            dayPlanList.add(dayPlan);
                        }
                    }
                    MealPlan mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, mealPlanStatus, dayPlanList);
                    list.add(mealPlan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int changeStatusById(String id) {
        int result = 0;
        String changeStatusSql = "update [dbo].[MealPlan]\n"
                + "set status = ?\n"
                + "where id = ?";
        String getStatusSql = "Select [status] from [dbo].[MealPlan] where id=?";
        try (Connection cn = JDBCUtil.getConnection();
                PreparedStatement getStatusPst = cn.prepareCall(getStatusSql);
                PreparedStatement changeStatusPst = cn.prepareCall(changeStatusSql);) {
            getStatusPst.setString(1, id);
            ResultSet table = getStatusPst.executeQuery();
            if (table != null) {
                while (table.next()) {
                    int status = table.getInt("status") == 1 ? 0 : 1;
                    changeStatusPst.setInt(1, status);
                    changeStatusPst.setString(2, id);
                    result = changeStatusPst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    public MealPlan getMealPlanById(String id) {
//        MealPlan mealPlan = null;
//        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId],[CustomerPlanId] from [dbo].[DayPlan]\n"
//                + "where MealPlanId = ? and status =1 order by dayInWeek Asc";
//        String getMealPlanByNameSql = "select [id],[type],[imgURL],[content],[status],[name] from [dbo].[MealPlan]\n"
//                + "  where id= ?";
//        try (Connection cn = JDBCUtil.getConnection();
//                PreparedStatement mealPlanPst = cn.prepareStatement(getMealPlanByNameSql);
//                PreparedStatement dayPlanPst = cn.prepareStatement(getDayPlanSql);) {
//            mealPlanPst.setString(1, id);
//            ResultSet mealPlanTable = mealPlanPst.executeQuery();
//            if (mealPlanTable != null) {
//                while (mealPlanTable.next()) {
//                    String mealPlanId = mealPlanTable.getString("id");
//                    String name = mealPlanTable.getString("name");
//                    String type = mealPlanTable.getString("type");
//                    String imgURL = mealPlanTable.getString("imgURL");
//                    String content = mealPlanTable.getString("content");
//                    int mealPlanStatus = mealPlanTable.getInt("status");
//                    List<DayPlan> dayPlanList = new ArrayList<>();
//                    dayPlanPst.setString(1, mealPlanId);
//                    ResultSet dayPlanTable = dayPlanPst.executeQuery();
//                    if (dayPlanTable != null) {
//                        while (dayPlanTable.next()) {
//                            String dayPlanId = dayPlanTable.getString("id");
//                            int dayInWeek = dayPlanTable.getInt("dayInWeek");
//                            int dayPlanStatus = dayPlanTable.getInt("status");
//                            String mealId = dayPlanTable.getString("MealId");
//                            // String id, String mealId, String mealPlanId, String customerPlanId, int dayInWeek, int status
//                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanId, null, dayInWeek, dayPlanStatus);
//                            dayPlanList.add(dayPlan);
//                        }
//                    }
//                    mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, mealPlanStatus, dayPlanList);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return mealPlan;
//    }
    public MealPlan getMealPlanById(String id) {
        MealPlan mealPlan = null;
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId] FROM [dbo].[DayPlan] "
                + "WHERE MealPlanId = ? ORDER BY dayInWeek ASC";
        String getMealPlanByIdSql = "SELECT [id],[type],[imgURL],[content],[status],[name] FROM [dbo].[MealPlan] "
                + "WHERE id = ?";

        try (Connection cn = JDBCUtil.getConnection();
                PreparedStatement mealPlanPst = cn.prepareStatement(getMealPlanByIdSql);
                PreparedStatement dayPlanPst = cn.prepareStatement(getDayPlanSql)) {

            mealPlanPst.setString(1, id);
            try (ResultSet mealPlanTable = mealPlanPst.executeQuery()) {
                if (mealPlanTable.next()) {
                    String mealPlanId = mealPlanTable.getString("id");
                    String name = mealPlanTable.getString("name");
                    String type = mealPlanTable.getString("type");
                    String imgURL = mealPlanTable.getString("imgURL");
                    String content = mealPlanTable.getString("content");
                    int mealPlanStatus = mealPlanTable.getInt("status");
                    List<DayPlan> dayPlanList = new ArrayList<>();

                    dayPlanPst.setString(1, mealPlanId);
                    try (ResultSet dayPlanTable = dayPlanPst.executeQuery()) {
                        while (dayPlanTable.next()) {
                            int dayPlanId = dayPlanTable.getInt("id");
                            int dayInWeek = dayPlanTable.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanTable.getInt("status");
                            String mealId = dayPlanTable.getString("MealId");
                            // String id, String mealId, String mealPlanId, String customerPlanId, int dayInWeek, int status
                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanId, -1, dayInWeek, dayPlanStatus);
                            dayPlanList.add(dayPlan);
                        }
                    }

                    mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, mealPlanStatus, dayPlanList);
                } else {
                    // Meal plan with the given ID was not found
                    System.err.println("Meal plan not found for id: " + id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally log the error to a logging framework or rethrow a custom exception
        }

        return mealPlan;
    }

    public int insertNewMealPlan(MealPlan mealPlan) {
        int result = 0;
        String insertSql = " insert [dbo].[MealPlan](id,type,imgURL,content,status,name) values(?,?,?,?,1,?)";
        String checkExistSql = "select [id] from [MealPlan] where id =?";
        Connection cn = null;

        try {
            String mealPlanId = mealPlan.getId();
            cn = JDBCUtil.getConnection();
            cn.setAutoCommit(false);
            if (cn != null) {
                PreparedStatement checkExistPst = cn.prepareStatement(checkExistSql);
                checkExistPst.setString(1, mealPlanId);
                ResultSet table = checkExistPst.executeQuery();
                if (!table.next()) {
                    PreparedStatement pst = cn.prepareStatement(insertSql);
                    pst.setString(1, mealPlan.getId().trim());
                    pst.setString(2, mealPlan.getType().trim());
                    pst.setString(3, mealPlan.getImgUrl().trim());
                    pst.setString(4, mealPlan.getContent().trim());
                    pst.setString(5, mealPlan.getName().trim());
                    result = pst.executeUpdate();
                    if (result > 0) {
                        cn.commit();
                    } else {
                        cn.rollback();
                    }
                } else {
                    cn.rollback();
                }
            }
        } catch (Exception e) {
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
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

    public Set<String> getAllMealPlanIds() {
        Set<String> idSet = new HashSet<>();
        String sql = "SELECT [Id] FROM [PRJ301].[dbo].[MealPlan]";
        try (Connection con = JDBCUtil.getConnection();
                Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                idSet.add(rs.getString(1));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return idSet;
    }

    public String checkValidMealPlanId(String id) {
        if (!id.matches("MP[0-9]{1,4}")) {
            return "Invalid ID pattern";
        } else if (getAllMealPlanIds().contains(id)) {
            return id + " already exists";
        } else {
            return null;
        }
    }

    public boolean updateMealPlan(MealPlan mealPlan) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtil.getConnection();
            if (conn != null) {
                String sql = "UPDATE MealPlan SET name = ?, type = ?, content = ?, imgUrl = ?, status = ? WHERE id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, mealPlan.getName());
                ps.setString(2, mealPlan.getType());
                ps.setString(3, mealPlan.getContent());
                ps.setString(4, mealPlan.getImgUrl());
                ps.setInt(5, mealPlan.getStatus());
                ps.setString(6, mealPlan.getId());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(MealPlanDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<MealPlan> getCustomerAllMealPlans() {
        ArrayList<MealPlan> list = new ArrayList<>();
        String getAllMealPlanSql = "SELECT [id],[name],[type],[imgURL],[content],[status] from [dbo].[MealPlan] where status=1";
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId] from [dbo].[DayPlan]\n"
                + "               where MealPlanId = ? order by dayInWeek Asc";
        try (Connection conn = JDBCUtil.getConnection();
                Statement statement = conn.createStatement();
                PreparedStatement pst = conn.prepareStatement(getDayPlanSql);) {

            ResultSet mealPlanRst = statement.executeQuery(getAllMealPlanSql);
            if (mealPlanRst != null) {
                while (mealPlanRst.next()) {
                    String mealPlanId = mealPlanRst.getString("id");
                    String name = mealPlanRst.getString("name");
                    String type = mealPlanRst.getString("type");
                    String imgURL = mealPlanRst.getString("imgURL");
                    String content = mealPlanRst.getString("content");
                    int mealPlanStatus = mealPlanRst.getInt("status");
                    List<DayPlan> dayPlanList = new ArrayList<>();
                    pst.setString(1, mealPlanId);
                    ResultSet dayPlanRst = pst.executeQuery();
                    if (dayPlanRst != null) {
                        while (dayPlanRst.next()) {
                            int dayPlanId = dayPlanRst.getInt("id");
                            int dayInWeek = dayPlanRst.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanRst.getInt("status");
                            String mealId = dayPlanRst.getString("MealId");
                            // String id, String mealId, String mealPlanId, String customerPlanId, int dayInWeek, int status
                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanId, -1, dayInWeek, dayPlanStatus);
                            dayPlanList.add(dayPlan);
                        }
                    }
                    // public MealPlan(String id, String name, String type, String content, String imgUrl, int status, List<DayPlan> dayPlanContains)
                    MealPlan mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, mealPlanStatus, dayPlanList);
                    list.add(mealPlan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MealPlan> getCustomerMealPlanListByType(String mpType, int quantity) {
        String sql1 = String.format("SELECT TOP %d [id], [name], [type], [imgURL], [content], [status] FROM [dbo].[MealPlan] WHERE [status] = 1 AND [type] LIKE ?", quantity);
        String sql2 = "SELECT [id], [dayInWeek], [status], [MealId], [MealPlanId] FROM [dbo].[DayPlan] WHERE [MealPlanId] = ? ORDER BY [dayInWeek] ASC";
        List<MealPlan> mealPlanList = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement st1 = con.prepareStatement(sql1);
                PreparedStatement st2 = con.prepareStatement(sql2)) {
            st1.setString(1, "%" + mpType + "%");
            ResultSet rs1 = st1.executeQuery();
            if (rs1 != null) {
                while (rs1.next()) {
                    String mealPlanId = rs1.getString(1);
                    String name = rs1.getString(2);
                    String type = rs1.getString(3);
                    String imgURL = rs1.getString(4);
                    String content = rs1.getString(5);
                    int status = rs1.getInt(6);
                    List<DayPlan> dayPlanList = new ArrayList<>();
                    st2.setString(1, mealPlanId);
                    ResultSet rs2 = st2.executeQuery();
                    if (rs2 != null) {
                        while (rs2.next()) {
                            int dayPlanId = rs2.getInt(1);
                            int dayInWeek = rs2.getInt(2);
                            int dayPlanStatus = rs2.getInt(3);
                            String mealId = rs2.getString(4);
                            String mealPlanIdFk = rs2.getString(5);
                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanIdFk, -1, dayInWeek, dayPlanStatus);
                            dayPlanList.add(dayPlan);
                        }
                    }
                    MealPlan mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, status, dayPlanList);
                    mealPlanList.add(mealPlan);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mealPlanList;
    }

    public MealPlan getCustomerMealPlanById(String id) {
        MealPlan mealPlan = null;
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId] FROM [dbo].[DayPlan] "
                + "WHERE MealPlanId = ? ORDER BY dayInWeek ASC";
        String getMealPlanByIdSql = "SELECT [id],[type],[imgURL],[content],[status],[name] FROM [dbo].[MealPlan] "
                + "WHERE id = ? and status =1 ";

        try (Connection cn = JDBCUtil.getConnection();
                PreparedStatement mealPlanPst = cn.prepareStatement(getMealPlanByIdSql);
                PreparedStatement dayPlanPst = cn.prepareStatement(getDayPlanSql)) {

            mealPlanPst.setString(1, id);
            try (ResultSet mealPlanTable = mealPlanPst.executeQuery()) {
                if (mealPlanTable.next()) {
                    String mealPlanId = mealPlanTable.getString("id");
                    String name = mealPlanTable.getString("name");
                    String type = mealPlanTable.getString("type");
                    String imgURL = mealPlanTable.getString("imgURL");
                    String content = mealPlanTable.getString("content");
                    int mealPlanStatus = mealPlanTable.getInt("status");
                    List<DayPlan> dayPlanList = new ArrayList<>();

                    dayPlanPst.setString(1, mealPlanId);
                    try (ResultSet dayPlanTable = dayPlanPst.executeQuery()) {
                        while (dayPlanTable.next()) {
                            int dayPlanId = dayPlanTable.getInt("id");
                            int dayInWeek = dayPlanTable.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanTable.getInt("status");
                            String mealId = dayPlanTable.getString("MealId");
                            // String id, String mealId, String mealPlanId, String customerPlanId, int dayInWeek, int status
                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanId, -1, dayInWeek, dayPlanStatus);
                            dayPlanList.add(dayPlan);
                        }
                    }

                    mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, mealPlanStatus, dayPlanList);
                } else {
                    // Meal plan with the given ID was not found
                    System.err.println("Meal plan not found for id: " + id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally log the error to a logging framework or rethrow a custom exception
        }

        return mealPlan;
    }

    public MealPlan getCustomizedMealPlan() {
        MealPlan mealPlan = null;
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId] FROM [dbo].[DayPlan] "
                + "WHERE MealPlanId = ? ORDER BY dayInWeek ASC";
        String getMealPlanByIdSql = "SELECT [id],[type],[imgURL],[content],[status],[name] FROM [dbo].[MealPlan] "
                + "WHERE id = 'MP999'";

        try (Connection cn = JDBCUtil.getConnection();
                PreparedStatement mealPlanPst = cn.prepareStatement(getMealPlanByIdSql);
                PreparedStatement dayPlanPst = cn.prepareStatement(getDayPlanSql)) {

            try (ResultSet mealPlanTable = mealPlanPst.executeQuery()) {
                if (mealPlanTable.next()) {
                    String mealPlanId = mealPlanTable.getString("id");
                    String name = mealPlanTable.getString("name");
                    String type = mealPlanTable.getString("type");
                    String imgURL = mealPlanTable.getString("imgURL");
                    String content = mealPlanTable.getString("content");
                    int mealPlanStatus = mealPlanTable.getInt("status");
                    List<DayPlan> dayPlanList = new ArrayList<>();

                    dayPlanPst.setString(1, mealPlanId);
                    try (ResultSet dayPlanTable = dayPlanPst.executeQuery()) {
                        while (dayPlanTable.next()) {
                            int dayPlanId = dayPlanTable.getInt("id");
                            int dayInWeek = dayPlanTable.getInt("dayInWeek");
                            int dayPlanStatus = dayPlanTable.getInt("status");
                            String mealId = dayPlanTable.getString("MealId");

                            // Create a new DayPlan object and add it to the list
                            DayPlan dayPlan = new DayPlan(dayPlanId, mealId, mealPlanId, -1, dayInWeek, dayPlanStatus);
                            dayPlanList.add(dayPlan);
                        }
                    }

                    // Create a new MealPlan object with the gathered information
                    mealPlan = new MealPlan(mealPlanId, name, type, content, imgURL, mealPlanStatus, dayPlanList);
                } else {
                    // Meal plan with the given ID was not found
                    System.err.println("Meal plan not found for id: MP999");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally log the error to a logging framework or rethrow a custom exception
        }
        return mealPlan;
    }
}
