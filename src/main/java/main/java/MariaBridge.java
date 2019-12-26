package main.java;

import javafx.util.Pair;

import java.sql.*;

public class MariaBridge {


    private static Connection conn;
    private static String tableName;

    public MariaBridge(String dbName, String dbUrl, String dbUser, String dbPass, String tableName) {
        this.tableName = tableName;

        this.connect(dbUrl + "?user=" + dbUser + "&password=" + dbPass);
    }

    private void connect(String url) {
        try {
            //STEP 2: Register JDBC driver

            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " "
                    + "(uuid nvarchar(36) NOT NULL, "
                    + " hash varbinary(256) NOT NULL, "
                    + " salt varbinary(256) NOT NULL)");

        } catch (Exception se) {
            se.printStackTrace();
        }


    }


    public void closeConnection() throws SQLException {
        if (conn != null) conn.close();
    }

    public void addEntry(String uuid, byte[] hash, byte[] salt) throws SQLException {

        String query = "INSERT INTO " + tableName + "(uuid,hash,salt) VALUES ('" + uuid + "',?,?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setBytes(1, hash);
        pstmt.setBytes(2, salt);
        pstmt.execute();
    }


    private Pair<byte[], byte[]> read(String uuid) throws SQLException {

        String query = "SELECT * FROM " + tableName + "";

        Statement statement = conn.createStatement();

        ResultSet resultSet = statement.executeQuery(query);

        byte[] hashBytes = null;
        byte[] saltBytes = null;
        while (resultSet.next()) {
            if (resultSet.getString("uuid").equals(uuid)) {
                hashBytes = resultSet.getBytes("hash");
                saltBytes = resultSet.getBytes("salt");
            }
        }

        return new Pair<byte[], byte[]>(hashBytes, saltBytes);
    }

    public boolean isStored(String uuid) throws SQLException {
        return (this.read(uuid).getKey() != null);
    }
}

