import java.sql.*;

public class Mysql {
    private Connection conn;
    public Mysql(String url , String user, String passwd) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( url, user, passwd);
            System.out.println("Database connected successfully!!!");
        } catch (ClassNotFoundException e) {
            conn = null;
            System.out.println("class not found");
        } catch (SQLException e) {
            conn = null;
            System.out.println("Can't connect to mysql, please try again! ");
        }
    }

    public void create(String sql) {
        try {
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void insertCsvData(final String csvDirectory,final String csvName) {
        String charset = "UTF-8";
        if (csvName.equals("room")) {
            charset = "GBK";
        }
        Connection CsvConn = CsvUtil.getConn(csvDirectory, charset);
        Statement stmt;
        try {
            // 读取CSV的数据
            stmt = CsvConn.createStatement();
            String sql = "SELECT DISTINCT * " + " FROM " + csvName;
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            // 创建对应的sql语句
            String insertSql = getInsertSql(rsmd, csvName);
            System.out.println("insert sql: " + insertSql);

            // 设置字符集
            conn.setAutoCommit(false);
            setCharset(csvName);
            // 向后端插入数据

            int count = 0;
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            while(rs.next()) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    pstmt.setString(i+1, rs.getString(rsmd.getColumnName(i+1)));
                }

                pstmt.addBatch();

                count++;

                if (count == 2000)
                {
                    count = 0;
                    pstmt.executeBatch();
                    conn.commit();
                }
            }
            pstmt.executeBatch();
            conn.commit();
            CsvUtil.close(CsvConn, stmt, rs );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Something wrong, please try again. ");
            return;
        }
    }

    public void insertSqliteData(final String url , final String tableName) {
        // 读取sqlite的数据，判断这个表格是否存在
        Connection SqliteConn = SqliteUtil.getConn(url);
        DatabaseMetaData metaData = null;
        Statement stmt;
        try {
            if (!containTable(SqliteConn, tableName)) {
                System.out.println(tableName + " doesn't exist. ");
                return;
            }
            // 存在之后开始拿数据
            stmt = SqliteConn.createStatement();
            String sql = "SELECT DISTINCT * " + " FROM " + tableName;
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            // 建立相应的sql语句
            String insertSql = getInsertSql(rsmd, tableName);
            System.out.println("insert sql: " + insertSql);

            // 设置字符集
            conn.setAutoCommit(false);
            setCharset(tableName);
            // 向后端插入数据

            int count = 0;
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            while(rs.next()) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    pstmt.setString(i+1, rs.getString(rsmd.getColumnName(i+1)));
                }

                pstmt.addBatch();

                count++;

                if (count == 2000)
                {
                    count = 0;
                    pstmt.executeBatch();
                    conn.commit();
                }
            }
            pstmt.executeBatch();
            conn.commit();
            SqliteUtil.close(SqliteConn, stmt, rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(final String tableName) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM " + tableName;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void dropTable(final String tableName) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "DROP TABLE " + tableName;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void select(final String tableName) {
        select(conn, tableName);
    }

    public void select(Connection connection , final String tableName) {
        Statement st = null;
        try {
            st = conn.createStatement();
            //执行SQL
            String sql = "select * from " + tableName;
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            //处理结果
            while(rs.next()){
                for (int i = 1; i < rsmd.getColumnCount()+1; i++) {
                    System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(rsmd.getColumnName(i)) + " ");
                }
                System.out.print("\n");
        }

        rs.close();
        st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getInsertSql(ResultSetMetaData rsmd, String tableName) throws SQLException {
        String column = "(";
        String questionMark = "(";
        for (int i = 1 ; i < rsmd.getColumnCount()+1; i++) {
            if (rsmd.getColumnName(i).equals("time")) {
                column += "exp" + rsmd.getColumnName(i) + ",";
                questionMark += "?,";
                continue;
            }
            column += rsmd.getColumnName(i) + ",";
            questionMark += "?,";
        }
        column = column.substring(0, column.length()-1) + ")";
        questionMark = questionMark.substring(0, questionMark.length()-1) + ")";

        // 建立相应的sql语句
        String insertSql = "insert into "+ tableName + " " + column + " values" + questionMark;
        System.out.println("insert sql: " + insertSql);

        return insertSql;
    }

    private void setCharset(String tableName) throws SQLException {
        conn.setAutoCommit(false);
        // 设置字符集
        String charsetSql = "alter table "+ tableName +" convert to character set  utf8;";
        PreparedStatement charsetStmt = conn.prepareStatement(charsetSql);
        charsetStmt.execute();
        conn.commit();
    }

    private boolean containTable(Connection connection , String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, "%", "%", new String[] { "TABLE" });
        boolean containFlag = false;
        while (rs.next()) {
            String tableContained = rs.getString("TABLE_NAME");
            if (tableContained.equals(tableName)) {
                containFlag = true;
                System.out.println("Contains table " + tableName);
            }
        }
        if (!containFlag) {
            System.out.println(tableName + " doesn't exist. ");
        }
        rs.close();
        return containFlag;
    }


}