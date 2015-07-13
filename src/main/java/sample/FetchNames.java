package sample;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.logging.Logger;

/**
 * Created by metairie on 10-Jul-15.
 */
public class FetchNames extends Task<ObservableList<String>> {
    private static final Logger LOG = Logger.getLogger(FetchNames.class.getName());

    @Override
    protected ObservableList<String> call() throws Exception {
        return ConnectDB.fetch();
    }
}
