import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CsvDemo {
    public static Map<String,String> parseUser(Connection conn, final String csvName){
        Statement stmt;
        Map<String,String> users = new HashMap<String, String>();
        try {
            stmt = conn.createStatement();
            // Select the ID and NAME columns from sample.csv
            final ResultSet rs = stmt.executeQuery("SELECT "
                    + " kdno,kcno,ccno,kdname,exptime,papername  " + " FROM " + csvName);
            while (rs.next()) {
                System.out.println("kdno: "+rs.getString("kdno"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }



    public static void parse(final String csvDirectory,final String csvName){
        Connection conn = CsvUtil.getConn(csvDirectory);
        parseUser(conn,csvName);
    }

    public static void main(String[] args) {
        parse("D:\\Programming\\UnivTest\\res\\", "room");
    }
}
