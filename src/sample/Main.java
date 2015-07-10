package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * This code is an example inspired by a piece of code found in github
 * <p>
 * Happy Thanks to Jewelsea
 * https://gist.github.com/jewelsea
 */
public class Main extends Application {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    @FXML
    VBox layout;
    @FXML
    VBox layoutCallableFuture;
    @FXML
    VBox layoutPollTake;

    public static void main(String[] args) {
        launch(args);
    }

    // initialize the program.
    // setting the database executor thread pool size to 1 ensures
    // only one database command is executed at any one time.
    @Override
    public void init() throws Exception {
        databaseExecutor = Executors.newFixedThreadPool(3, new DBThreadFactory());
        // run the database setup in parallel to the JavaFX application setup.
        DBSetupTask setup = new DBSetupTask();
        databaseSetupFuture = databaseExecutor.submit(setup);
    }

    // start showing the UI.
    @Override
    public void start(Stage stage) throws InterruptedException, ExecutionException, IOException, SQLException {
        GridPane root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        initOldTaskManner(root); // example given by Jewelsea
        initOldCallableManner(root); // Future+Callable = blocking javafx UI thread
        initOldCompletionServiceManner(root); // poll/take

//        initNewTaskManner(root); // Completable Future

        Scene scene = new Scene(root, 1280, 768);
        stage.setScene(scene);
        stage.show();
    }

    // shutdown the program.
    @Override
    public void stop() throws Exception {
        databaseExecutor.shutdown();
        if (!databaseExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
            LOG.info("Database execution thread timed out after 3 seconds rather than shutting down cleanly.");
        }
    }

    //-------------------------------
    //
    //-------------------------------

    private void initOldCompletionServiceManner(GridPane root) throws SQLException {
        final ListView<String> listView = new ListView<>();
        final ProgressIndicator databaseActivityIndicator = new ProgressIndicator();
        databaseActivityIndicator.setVisible(false);
        final Button clearNameList = new Button("Clear");
        clearNameList.setOnAction(event -> listView.getItems().clear());
        final Button fetchNames = new Button("Fetch names Callable");
        fetchNames.setOnAction(event -> {
                    CompletionService<ObservableList<String>> completionService = new ExecutorCompletionService<>(databaseExecutor);
                    // http://markusjais.com/understanding-java-util-concurrent-completionservice/
//                    ObservableList<Callable<String>> callables =
                    completionService.submit(() -> FetchNames.fetch());
                }
        );

        // try to find vbox layout
        for (int i = 0; i < root.getChildren().size(); i++) {
            String id = root.getChildren().get(i).getId();
            if (id != null && id.equalsIgnoreCase("layoutPollTake")) {
                layoutPollTake = (VBox) root.getChildren().get(i);
            }
        }
        layoutPollTake.setStyle("-fx-background-color: lightskyblue; -fx-padding: 15;");
        layoutPollTake.getChildren().setAll(new HBox(10, fetchNames, clearNameList, databaseActivityIndicator), listView);
        layoutPollTake.setPrefHeight(100);
    }

    private static ObservableList<Callable<String>> createCallableList() {
        ObservableList<Callable<String>> callables = FXCollections.emptyObservableList();
        for (int i = 0; i < 10; i++) {
            callables.add(null); // implement this below;
        }
        return callables;
    }

    /*public class LongRunningTask implements Callable<String> {

        public String call() {
            // do stuff and return some String
            try {
                Thread.sleep(Math.abs(new Random().nextLong() % 5000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return Thread.currentThread().getName();
        }
    }*/
    //-------------------------------
    // Future + Callable - blocking the JavaFX UI thread
    //-------------------------------

    private void initOldCallableManner(GridPane root) {
        final ListView<String> listView = new ListView<>();
        final ProgressIndicator databaseActivityIndicator = new ProgressIndicator();
        databaseActivityIndicator.setVisible(false);
        final Button clearNameList = new Button("Clear");
        clearNameList.setOnAction(event -> listView.getItems().clear());
        final Button fetchNames = new Button("Fetch names Callable");
        fetchNames.setOnAction(event -> {
                    FetchNamesCallable callable = new FetchNamesCallable();
                    Future<ObservableList<String>> future = databaseExecutor.submit(callable);
                    try {
                        listView.setItems(future.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );

        // try to find vbox layout
        for (int i = 0; i < root.getChildren().size(); i++) {
            String id = root.getChildren().get(i).getId();
            if (id != null && id.equalsIgnoreCase("layoutCallableFuture")) {
                layoutCallableFuture = (VBox) root.getChildren().get(i);
            }
        }
        layoutCallableFuture.setStyle("-fx-background-color: sandybrown; -fx-padding: 15;");
        layoutCallableFuture.getChildren().setAll(new HBox(10, fetchNames, clearNameList, databaseActivityIndicator), listView);
        layoutCallableFuture.setPrefHeight(100);
    }


    //-------------------------------
    // old Task
    //-------------------------------

    // executes database operations concurrent to JavaFX operations.
    private ExecutorService databaseExecutor;
    // the future's data will be available once the database setup has been complete.
    private Future databaseSetupFuture;

    private void initOldTaskManner(GridPane root) throws ExecutionException, InterruptedException {
        // wait for the database setup to complete cleanly before showing any UI.
        // a real app might use a preloader or show a splash screen if this
        // was to take a long time rather than just pausing the JavaFX application thread.
        databaseSetupFuture.get();

        final ListView<String> listView = new ListView<>();
        final ProgressIndicator databaseActivityIndicator = new ProgressIndicator();
        databaseActivityIndicator.setVisible(false);
        final Button clearNameList = new Button("Clear");
        clearNameList.setOnAction(event -> listView.getItems().clear());
        final Button fetchNames = new Button("Fetch old Future Task");
        fetchNames.setOnAction(event -> {
                    final FetchNamesTask fetchNamesTask = new FetchNamesTask();
                    fetchNames.disableProperty().bind(fetchNamesTask.runningProperty());
                    clearNameList.disableProperty().bind(fetchNamesTask.runningProperty());
                    databaseActivityIndicator.visibleProperty().bind(fetchNamesTask.runningProperty());
                    databaseActivityIndicator.progressProperty().bind(fetchNamesTask.progressProperty());
                    fetchNamesTask.setOnSucceeded(t -> listView.setItems(fetchNamesTask.getValue()));
                    databaseExecutor.submit(fetchNamesTask);
                }
        );

        // try to find vbox layout
        for (int i = 0; i < root.getChildren().size(); i++) {
            String id = root.getChildren().get(i).getId();
            if (id != null && id.equalsIgnoreCase("layout")) {
                layout = (VBox) root.getChildren().get(i);
            }
        }
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 15;");
        layout.getChildren().setAll(new HBox(10, fetchNames, clearNameList, databaseActivityIndicator), listView);
        layout.setPrefHeight(100);
    }


}