package sample;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public ProgressBar bar;

    static final int max = 10000000;
    Task task = new Task<Void>() {
        @Override
        public Void call() {
            for (int i = 1; i <= max; i++) {
                if (isCancelled()) {
                    break;
                }
                updateProgress(i, max);
            }
            return null;
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bar.progressProperty().bind(task.progressProperty());
    }

    public void click(ActionEvent actionEvent) {
        new Thread(task).start();
    }

}


