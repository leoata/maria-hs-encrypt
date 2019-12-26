package main.java;

import java.sql.SQLException;

public class sData {

    private MariaBridge mariaBridge;
    private Crypt crypt;

    public sData(String dbUrl, String dbName, String dbUser, String dbPass, String tableName, int iterations, String encryptionAlgorithm) {
        this.crypt = new Crypt(iterations, encryptionAlgorithm);
        this.mariaBridge = new MariaBridge(dbName, dbUrl, dbUser, dbPass, tableName);
    }

    public void shutdown() throws SQLException {
        mariaBridge.closeConnection();
    }

    public MariaBridge getMariaBridge() {
        return mariaBridge;
    }

    public Crypt getCrypt() {
        return crypt;
    }

}
