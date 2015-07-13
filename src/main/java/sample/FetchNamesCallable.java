package sample;

import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class FetchNamesCallable implements Callable<ObservableList<String>> {
    private static final Logger LOG = Logger.getLogger(FetchNamesTask.class.getName());

    @Override
    public ObservableList<String> call() throws Exception {
        return fetchNames();
    }

    private ObservableList<String> fetchNames() throws SQLException {
        return ConnectDB.fetch();
    }
}