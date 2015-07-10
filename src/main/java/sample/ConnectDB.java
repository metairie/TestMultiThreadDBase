package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by metairie on 10-Jul-15.
 */
public class ConnectDB {
    private static final Logger LOG = Logger.getLogger(ConnectDB.class.getName());

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        LOG.info("Getting a database connection");
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://neosdbdev:3306/neos", "root", "eurovision");
    }
}
