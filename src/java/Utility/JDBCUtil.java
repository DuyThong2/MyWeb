package Utility;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCUtil {
    
    public static void main(String[] args) {
        
        try {
            Connection connection = JDBCUtil.getConnection();
            JDBCUtil.printInfo(connection);
            closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception ex) {
            Logger.getLogger(JDBCUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static Connection getConnection() throws Exception{
        Connection cn=null;
        String IP="localhost";
        String instanceName="ADMIN\\SQLEXPRESS";
        String port="1433";
        String uid="sa";
        String pwd="12345";
        String db="prj301";
        String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url="jdbc:sqlserver://" +IP+"\\"+ instanceName+":"+port
                 +";databasename="+db+";user="+uid+";password="+pwd;
        Class.forName(driver);
        cn=DriverManager.getConnection(url);
        return cn;
    }

    public static void closeConnection (Connection c) {
        try {

            if(c!=null) {
                c.close();
            }

        }catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void printInfo(Connection c) throws SQLException {
        try{
            if(c!=null) {
                DatabaseMetaData mtdt = c.getMetaData();
                System.out.println(mtdt.getDatabaseProductName());
                System.out.println(mtdt.getDatabaseProductVersion());
                System.out.println(c.getMetaData().toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
