package sample;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by metairie on 10-Jul-15.
 */
public class DBThreadFactory implements ThreadFactory {
    static final AtomicInteger poolNumber = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "Database-Connection-" + poolNumber.getAndIncrement() + "-thread");
        thread.setDaemon(true);
        return thread;
    }
}