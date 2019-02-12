package sample;

import javafx.collections.ObservableList;

import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class FetchNamesTask extends DBTask<ObservableList<String>> {
    private static final Logger LOG = Logger.getLogger(FetchNamesTask.class.getName());

    @Override
    protected ObservableList<String> call() throws InterruptedException {
        return fetchNames();
    }

    private ObservableList<String> fetchNames() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ConnectDB.fetch();
    }
}