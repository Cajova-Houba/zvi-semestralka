package zvi.valesz.app.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import zvi.valesz.app.core.Core;

import java.io.File;

public class Controller {

    @FXML
    private ImageView imageView;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Slider thresholdSlider;

    @FXML
    private Label thresholdValLabel;

    public void initialize() {
        thresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                int threshold = (int)Math.ceil((double)newValue);

                thresholdValLabel.setText(Integer.toString(threshold));
            }
        });
    }


    @FXML
    public void onLoadImageClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File f = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if (f != null) {
            Image image = new Image(f.toURI().toString());
            imageView.setImage(image);
        }
    }

    @FXML
    public void performSegmentation() {
        Image image = imageView.getImage();
        if(image == null) {
            System.out.println("No image!");
            return;
        }

        image = Core.performRegionGrowing(image,(float)thresholdSlider.getValue());
        imageView.setImage(image);
    }
}
