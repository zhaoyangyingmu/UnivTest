import java.sql.*;

public class JavaTest {
    public static void main(String[] args) {
        String URL="jdbc:mysql://127.0.0.1:3306/university_test?useUnicode=true&characterEncoding=utf8";
        String USER="root";
        String PASSWORD="167127";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=DriverManager.getConnection(URL, USER, PASSWORD);
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from user");
            while(rs.next()){
                System.out.println(rs.getString("username")+" "+rs.getString("user_password"));
            }
            rs.close();
            st.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
