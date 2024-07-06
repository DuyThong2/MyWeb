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

/**
 *
 * @author ASUS
 */
public class MealPlanDAO {

    public ArrayList<MealPlan> getAllMealPlans() {
        ArrayList<MealPlan> list = new ArrayList<>();
        String getAllMealPlanSql = "SELECT [id],[name],[type],[imgURL],[content],[status] from [dbo].[MealPlan] order by status desc";
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId],[CustomerPlanId] from [dbo].[DayPlan]\n"
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

    public ArrayList<MealPlan> getAllMeanLanByName(String searchName) {
        ArrayList<MealPlan> list = new ArrayList<>();
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId],[CustomerPlanId] from [dbo].[DayPlan]\n"
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
        String getDayPlanSql = "SELECT [id],[dayInWeek],[status],[MealId],[MealPlanId],[CustomerPlanId] FROM [dbo].[DayPlan] "
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

}
