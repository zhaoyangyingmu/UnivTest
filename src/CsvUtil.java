import java.sql.*;
import java.util.Properties;

public class CsvUtil {
    public static Connection getConn(String csvDirectory, String charset){
        // 加载CSV-JDBC驱动
        try {
            Class.forName("org.relique.jdbc.csv.CsvDriver");
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 解析CSV前的一些准备工作：解析参数设置
        final Properties props = new java.util.Properties();

        // 该CSV的数据是由','分隔
        props.put("separator", ",");

        // 首行(去掉上面头行后的第一行)包含数据
        props.put("suppressHeaders", "false");

        // 要解析的文件类型
        props.put("fileExtension", ".csv");

        // 字符集
        props.put("charset", charset);

        // 创建一个connection. The first command line parameter is assumed to
        // be the directory in which the .csv files are held
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:relique:csv:" + csvDirectory, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
