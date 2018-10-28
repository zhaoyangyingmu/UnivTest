import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.sql.*;

public class SQLiteTest {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:D:\\Programming\\UnivTest\\res\\xxxdatabase.db");
            Statement stat = conn.createStatement();

            ResultSet rs = stat.executeQuery("select * from student");
            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    System.out.println(rsmd.getColumnName(i+1) + ":" + rs.getString(rsmd.getColumnName(i+1)));
                }
                System.out.println("--------------------------------------------");
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
