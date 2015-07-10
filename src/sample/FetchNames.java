package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Created by metairie on 10-Jul-15.
 */
public class FetchNames {
    private static final Logger LOG = Logger.getLogger(FetchNames.class.getName());

    public static ObservableList<String> fetch() throws SQLException {
        LOG.info("Fetching names from database");
        ObservableList<String> names = FXCollections.observableArrayList();
        Statement st = null;
        try {
            st = ConnectDB.getConnection().createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ResultSet rs = st.executeQuery("select name from category");
        while (rs.next()) {
            names.add(rs.getString("name"));
        }
        LOG.info("Found " + names.size() + " names");
        return names;
    }
}
