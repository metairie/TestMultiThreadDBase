package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DBSetupTask extends DBTask {
    private static final Logger LOG = Logger.getLogger(DBSetupTask.class.getName());

    @Override
    protected Void call() throws Exception {
        try (Connection con = ConnectDB.getConnection()) {
            if (!schemaExists(con)) {
                createSchema(con);
                populateDatabase(con);
            }
        }
        return null;
    }

    private boolean schemaExists(Connection con) {
        LOG.info("Checking for Schema existence");
        try {
            Statement st = con.createStatement();
            st.executeQuery("select count(*) from city");
            LOG.info("Schema exists");
        } catch (SQLException ex) {
            LOG.info("Existing DB not found will create a new one");
            return false;
        }
        return true;
    }

    private void createSchema(Connection con) throws SQLException {
        LOG.info("Creating schema");
           /* Statement st = con.createStatement();
            String table = "create table employee(id integer, name varchar(64))";
            st.executeUpdate(table);*/
        LOG.info("Created schema");
    }

    private void populateDatabase(Connection con) throws SQLException {
        LOG.info("Populating database");
            /*Statement st = con.createStatement();
            for (String name: SAMPLE_NAME_DATA) {
                st.executeUpdate("insert into employee values(1,'" + name + "')");
            }*/
        LOG.info("Populated database");
    }

}