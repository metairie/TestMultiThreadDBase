package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Logger;

/**
 * Created by metairie on 10-Jul-15.
 */
public class ConnectDB {
    private static final Logger LOG = Logger.getLogger(ConnectDB.class.getName());

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        LOG.info("Getting a database connection");
        Class.forName("com.mysql.jdbc.Driver");
//        return DriverManager.getConnection("jdbc:mysql://localhost:3306/neos", "root", "eurovision");
        return DriverManager.getConnection("jdbc:mysql://db4free.net:3306/metairie", "rootmetairie", "eurovision");
    }

    public static ObservableList<String> fetch() {
        LOG.info("Fetching names from database");
        Connection con = null;
        ObservableList<String> names = FXCollections.observableArrayList();
        try {
            con = ConnectDB.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select name from city LIMIT 500");
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
            LOG.info("Found " + names.size() + " names");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
}
