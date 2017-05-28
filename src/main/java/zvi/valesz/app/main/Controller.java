package zvi.valesz.app.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zvi.valesz.app.core.Core;
import zvi.valesz.app.core.Statistics;
import zvi.valesz.app.core.region.MergedRegion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo: use scroll pane or new window for source image
public class Controller {

    @FXML
    private ImageView segmentedImageView;

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
        // todo: choose the way of colorization
        Image image = imageView.getImage();
        if(image == null) {
            System.out.println("No image!");
            return;
        }

        Map<String, Object> statistics = new HashMap<>();
        float threshold = (float)thresholdSlider.getValue();
        List<MergedRegion> regions = Core.performRegionGrowing(image,threshold, statistics);
        Image segmentedImage = Core.colorize(image, regions);
        int[] histogram = Core.calculateHistogram(segmentedImage);
        statistics.put(Statistics.HISTOGRAM, histogram);

        try {
            displaySegmentedImageInNewWindowFxml(segmentedImage, statistics);
        } catch (IOException e) {
            e.printStackTrace();
            displaySegmentedImageInNewWindow(segmentedImage, statistics);
        }
    }

    /**
     * Displays the image in new window. Used to display original image converted to grey scale.
     * @param greyImage
     */
    private void displayGreyImageInNewWindow(Image greyImage) {
        final Stage dialog = new Stage();
        VBox pane = new VBox(20);
        pane.getChildren().add(new Label("Obrázek v šedotónu:"));
        pane.getChildren().add(new ImageView(greyImage));
        Scene dialogScene = new Scene(pane, Math.max(greyImage.getWidth(),200), greyImage.getHeight()+100);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Displays the segmented image.
     * @param segmentedImage
     * @param statistics A map containing statistics data.
     */
    private void displaySegmentedImageInNewWindow(Image segmentedImage, Map<String,Object> statistics) {
        float threshold = (float)statistics.get(Statistics.THRESHOLD);
        int numOfRegions = (int)statistics.get(Statistics.MERGED_REGIONS_COUNT);
        int totalNumOfRegions = (int)statistics.get(Statistics.TOTAL_REGIONS_COUNT);

        // todo: save button for image and maybe other data?
        final Stage newWindow = new Stage();
        newWindow.initModality(Modality.NONE);
//            dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Label("Segmentovaný obrázek:"));
        dialogVbox.getChildren().add(new Label("Threshold: "+threshold));
        dialogVbox.getChildren().add(new Label("Nalezených oblastí před narůstáním" + totalNumOfRegions));
        dialogVbox.getChildren().add(new Label("Nalezených oblastí: "+numOfRegions));
        dialogVbox.getChildren().add(new ImageView(segmentedImage));
        Scene dialogScene = new Scene(dialogVbox, Math.max(segmentedImage.getWidth(),200), segmentedImage.getHeight()+200);
        newWindow.setScene(dialogScene);
        newWindow.show();
    }


    private void displaySegmentedImageInNewWindowFxml(Image segmentedImage, Map<String, Object> statistics) throws IOException {
        final Stage newWindow = new Stage();
        newWindow.initModality(Modality.NONE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("imgWindow.fxml"));
        Parent root = loader.load();

        newWindow.setTitle("Segmentovaný obraz");
        newWindow.setScene(new Scene(root, 600,400));
        loader.<ImgWindowController>getController().init(segmentedImage, statistics);
        newWindow.show();
    }
}
