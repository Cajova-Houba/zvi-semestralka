package zvi.valesz.app.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Parent root;
    private static FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        root = loader.load();
        primaryStage.setTitle("Segmentace obrazu");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        loader.<Controller>getController().stopSegmentationThread();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
