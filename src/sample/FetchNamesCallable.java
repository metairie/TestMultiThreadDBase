package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class FetchNamesCallable implements Callable<ObservableList<String>> {
    private static final Logger LOG = Logger.getLogger(FetchNamesTask.class.getName());

    @Override
    public ObservableList<String> call() throws Exception {
        Thread.sleep(1000);
        try (Connection con = ConnectDB.getConnection()) {
            return fetchNames(con);
        }
    }

    private ObservableList<String> fetchNames(Connection con) throws SQLException {
        LOG.info("Fetching names from database");
        ObservableList<String> names = FXCollections.observableArrayList();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select name from category");
        while (rs.next()) {
            names.add(rs.getString("name"));
        }
        LOG.info("Found " + names.size() + " names");
        return names;
    }
}