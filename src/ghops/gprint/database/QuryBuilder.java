package ghops.gprint.database;

import ghops.gprint.tools.Config;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuryBuilder {

    private Connection connection = null;

    private PreparedStatement statement;

    public QuryBuilder() {
        this.connect();
    }

    public Connection connect() {

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

    public PreparedStatement getStatement() {
        return this.statement;
    }

    public ResultSet getResult(PreparedStatement statement) {
        //List<Fabric> list = new ArrayList();
        //String query = "SELECT fabrics.*, printtype.name AS printTypeName FROM fabrics LEFT JOIN printtype ON printtype.id=fabrics.type ORDER BY id DESC";
        try {
            //PreparedStatement statement = connect().prepareStatement(query);
            return statement.executeQuery();

        } catch (SQLException ex) {
            System.err.println(ex);
            return null;
        }
    }

}
