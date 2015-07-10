package sample;

import javafx.concurrent.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DBTask<T> extends Task<T> {
    private static final Logger LOG = Logger.getLogger(DBTask.class.getName());

    DBTask() {
        setOnFailed(t -> LOG.log(Level.SEVERE, null, getException()));
    }
}
