import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProcessData {
    private static Mysql mysql;
    public static void main(String[] args) {
        connect();
        help();
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("type in your command without \";\"");
            String line = input.nextLine();
            if (line.equals("close")) {
                close();
                break;
            }
            else if (line.equals("create")) {
                create("D:/Programming/UnivTest/res/create.sql");
            }
            else if (line.contains("insert csv")) {
                String[] result = line.split(" ");
                String tableName = result[result.length-1];
                deleteData(tableName);
                insertCsvData("D:\\Programming\\UnivTest\\res\\", tableName);
            }
            else if (line.contains("insert sqlite")) {
                String[] result = line.split(" ");
                String tableName = result[result.length-1];
                deleteData(tableName);
                insertSqliteData("D:\\Programming\\UnivTest\\res\\xxxdatabase.db", tableName);
            }
            else if (line.contains("delete")) {
                String[] result = line.split(" ");
                String tableName = result[result.length-1];
                deleteData(tableName);
            }
            else if (line.contains("drop")) {
                String[] result = line.split(" ");
                String tableName = result[result.length-1];
                dropTable(tableName);
            }
            else if (line.contains("select")) {
                String[] result = line.split(" ");
                String tableName = result[result.length-1];
                select(tableName);
            }
        }
    }

    private static void connect() {
        // get mysql
        String url = "jdbc:mysql://localhost:3306/lab1?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String passwd = "123456";
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
            System.out.println("File not found, please try again.");
            return;
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

    private static void close() {
        mysql.close();
    }

    private static void help() {
        String mes = "Command as follows: \n" +
                "type in create to create tables\n" +
                "type in insert csv [table] to insert csv data\n" +
                "type in insert sqlite [table] to insert sqlite data\n" +
                "type in delete from [table] to delete data in table\n" +
                "type in drop [table] to drop table\n" +
                "type in select * from [table] to check data\n" +
                "type in close to close connection";
        System.out.println(mes);
    }
}
