import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProcessData {
    private static Mysql mysql;
    public static void main(String[] args) {
        connect();
        select("student");
//        String filePath = "D:/Programming/UnivTest/res/create.sql";
//        create(filePath);

        //deleteData("student");
        //insertCsvData("D:\\Programming\\UnivTest\\res\\", "room");
        //insertCsvData("D:\\Programming\\UnivTest\\res\\", "student");
        //insertSqliteData("D:\\Programming\\UnivTest\\res\\xxxdatabase.db", "student");
        //insertSqliteData("D:\\Programming\\UnivTest\\res\\xxxdatabase.db", "room");


    }

    private static void connect() {
        // get mysql
        String url = "jdbc:mysql://localhost:3306/university_test?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String passwd = "167127";
        mysql = new Mysql(url, user, passwd);
    }

    private static void create(String filePath) {
        // create table
        File file  = new File(filePath);
        try {
            Scanner in = new Scanner(file);
            String content = "";
            while (in.hasNext()) {
                content += in.nextLine();
            }
            String[] createSqls = content.split(";");
            for (int i = 0 ; i <createSqls.length; i++) {
                mysql.create(createSqls[i] + ";");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void insertCsvData(final String csvDirectory,final String csvName) {
        mysql.insertCsvData(csvDirectory, csvName);
    }

    private static void insertSqliteData(final String url, final String tableName) {
        mysql.insertSqliteData(url , tableName);
    }

    private static void deleteData(String tableName) {
        mysql.deleteData(tableName);
    }

    private static void select(String tableName) {
        mysql.select(tableName);
    }

    private static void dropTable(final String tableName) {
        mysql.dropTable(tableName);
    }
}
