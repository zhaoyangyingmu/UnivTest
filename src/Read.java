import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Read {
    private static Connection conn;
    public static void main(String[] args) throws IOException, SQLException {
        long startTime = System.currentTimeMillis();

        File file = new File("D:/Programming/UnivTest/res/student.csv");

        Scanner in = new Scanner(file);

        getConnect();
        System.out.println("数据库连接成功");
        insert_data(in);

        long EndTime = System.currentTimeMillis();
        long time = (EndTime - startTime) / 1000;

        System.out.println("导入数据共用时：" + time);
    }
    private static void insert_data(Scanner in) throws SQLException {
        conn.setAutoCommit(false);
        // 设置字符集
        String charsetSql = "alter table student convert to character set  utf8;";
        PreparedStatement charsetStmt = conn.prepareStatement(charsetSql);
        charsetStmt.execute();
        conn.commit();


        int count = 0;
        int insert_size = 6;
//        String sql = "insert into room " +
//                "(kdno,kcno,ccno,kdname,exptime)"
//                + "values(?,?,?,?,?)";
        String sql = "insert into student " +
                "(registno, name, kdno, kcno , ccno, seat)"
                + "values(?,?,?,?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        in.next();
        while (in.hasNext())
        {
            String temp1 = in.nextLine();
            String[] temp = temp1.split(",");
            if(temp.length != insert_size) {
                continue;
            }
            //temp[4] += ":00";
            // todo 乱码问题
            //temp[3] = "复旦大学";
            for (int i = 0; i < insert_size; i++) {
                temp[i] = temp[i].substring(1, temp[i].length()-1);
                pstmt.setString(i+1, temp[i]);
            }

            try {
                pstmt.execute();
                conn.commit();
            } catch (MySQLIntegrityConstraintViolationException e) {
                System.out.println("Skip");
            }
//
//            pstmt.addBatch();
//
//            count++;
//
//            if (count == 20000)
//            {
//                count = execute(pstmt);
//            }
        }
//        pstmt.executeBatch();
//        conn.commit();
    }

    public static int execute(PreparedStatement pstmt) throws SQLException
    {
        pstmt.executeBatch();
        conn.commit();
        return 0;
    }

    private static void getConnect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/university_test?useUnicode=true&characterEncoding=utf8",
            "root", "167127");
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }

//    private static void deleteFileDuplication(String path) {
//        FileReader fileReader = null;
//        try {
//            fileReader = new FileReader(new File(path));
//        } catch (FileNotFoundException e) {
//            System.out.println("No such file exist. ");
//        }
//        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        Map<String, String> map = new HashMap<String, String>();
//        String readLine = null;
//        int i = 0;
//
//        while ((readLine = bufferedReader.readLine()) != null) {
//            // 每次读取一行数据，与 map 进行比较，如果该行数据 map 中没有，就保存到 map 集合中
//            if (!map.containsValue(readLine)) {
//                map.put("key" + i, readLine);
//                i++;
//            }
//        }
//
//        for (int j = 0; j < map.size(); j++) {
//            System.out.println(map.get("key" + j));
//        }
//    }


}
