package ghops.gprint.database;

import ghops.gprint.tools.Config;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DBConnect {

    public DBConnect() {
        connect();
    }

    private static Connection connection = null;

    public static Connection connect() {

        if (connection != null) {
            return connection;
        }
        System.out.println("vt sunucusuna bağlanıldı!");

        try {
            Config cfg = new Config();
            Properties prop = cfg.getProperties();
            String host = prop.getProperty("host");

            String database = prop.getProperty("database");
            String port = prop.getProperty("port");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");

            connection = DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + database + "?characterEncoding=utf8", user, password);
 

        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return connection;
    }

    public void disonnect() throws SQLException {
        connection.close();
        connection = null;
    }

}
