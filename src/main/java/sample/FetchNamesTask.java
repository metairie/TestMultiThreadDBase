package sample;

import javafx.collections.ObservableList;

import java.util.logging.Logger;

public class FetchNamesTask extends DBTask<ObservableList<String>> {
    private static final Logger LOG = Logger.getLogger(FetchNamesTask.class.getName());

    @Override
    protected ObservableList<String> call() throws InterruptedException {
        return fetchNames();
    }

    private ObservableList<String> fetchNames() {
        return ConnectDB.fetch();
    }
}