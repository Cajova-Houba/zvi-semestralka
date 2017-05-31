package zvi.valesz.app.main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zvi.valesz.app.core.ColorizationMethod;
import zvi.valesz.app.core.Core;
import zvi.valesz.app.core.Statistics;
import zvi.valesz.app.core.Threshold;
import zvi.valesz.app.core.region.MergedRegion;
import zvi.valesz.app.core.region.Region;

import java.io.File;
import java.io.IOException;
import java.util.*;

// todo: use scroll pane or new window for source image
public class Controller {

    @FXML
    private ImageView imageView;

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField thresholdValLabel;

    @FXML
    private Label feedback;

    @FXML
    private TextField newThresh;

    @FXML
    private TextField threshVal;

    @FXML
    private ListView<Threshold> thresholdView;

    @FXML
    private RadioButton autoThreshRadio;

    @FXML
    private RadioButton manualThreshRadio;

    @FXML
    private final ToggleGroup manThreshGroup = new ToggleGroup();

    @FXML
    private ScrollPane imageContainer;

    @FXML
    private RadioButton brightColorsRadio;

    @FXML
    private ProgressBar segmentationProgress;

    @FXML
    private Button segmentationBtn;

    @FXML
    private Button stopSegmentationBtn;

    /**
     * Thresholds sorted by threshold value. Map is used to avoid duplicities.
     */
    private Map<Integer, Threshold> thresholdMap;


    private Thread segmentationThread;

    public void onRemoveThresholdClick() {
        int i = thresholdView.getSelectionModel().getSelectedIndex();
        if(i < 0) {
            displayFeedbackMessage("Není vybrán žádný práh.");
            return;
        }

        Threshold t = thresholdView.getSelectionModel().getSelectedItem();
        thresholdMap.remove(t.threshold);
        thresholdView.setItems(FXCollections.observableList(new ArrayList<>(thresholdMap.values())));
    }

    public void onStopSegmentationClick() {
        stopSegmentationThread();
        segmentationProgress.setProgress(0.100);
        segmentationProgress.setVisible(false);
        segmentationBtn.setDisable(false);
        stopSegmentationBtn.setVisible(false);
        displayFeedbackMessage("Segmentace zastavena.");
    }

    public void stopSegmentationThread() {
        if(segmentationThread == null) {
            return;
        }

        if(!segmentationThread.isAlive()) {
            return;
        }

        segmentationThread.stop();
    }

    public void onAddThresholdClick() {
        int nt = 0;
        int tv = 0;

        // load new threshold
        String ntStr = newThresh.getText();
        String tvStr = threshVal.getText();
        try{
            nt = Integer.parseInt(ntStr);
        } catch (NullPointerException ex) {
            displayFeedbackMessage("Není zadán práh.");
            return;
        } catch (NumberFormatException ex) {
            displayFeedbackMessage(ntStr+" není platné číslo.");
            return;
        }
        try {
            tv = Integer.parseInt(tvStr);
        } catch (NullPointerException ex) {
            displayFeedbackMessage("Není zadána nová hodnota prahu.");
            return;
        } catch (NumberFormatException ex) {
            displayFeedbackMessage(tvStr + " není platné číslo.");
            return;
        }

        // check the threshold
        Threshold threshold = new Threshold(nt,tv);
        if(threshold.threshold < 0 || threshold.threshold > 256) {
            displayFeedbackMessage("Práh "+threshold.threshold+" není v intervalu <0;255>.");
            return;
        }
        if(threshold.newValue < 0 || threshold.newValue > 255) {
            displayFeedbackMessage("Nová hodnota "+threshold.newValue+" není v intervalu <0;255>.");
            return;
        }

        // add it to display
        if(thresholdMap == null) {
            thresholdMap = new TreeMap<>();
        }
        thresholdMap.put(threshold.threshold, threshold);
        thresholdView.setItems(FXCollections.observableList(new ArrayList<>(thresholdMap.values())));
    }

    @FXML
    public void performManualThresholding() {
        Image image = imageView.getImage();
        boolean auto = autoThreshRadio.isSelected();
        List<Threshold> thresholds = new ArrayList<>();

        if (image == null) {
            displayFeedbackMessage("Není žádný obrázek.");
            return;
        }
        if(image.getWidth() == 0 || image.getHeight() == 0) {
            displayFeedbackMessage("Špatné rozměry obrázku.");
            return;
        }
        if(!auto){
            if (thresholdMap == null || thresholdMap.isEmpty()) {
                displayFeedbackMessage("Nejsou zadány žádné prahy.");
                return;
            } else {
                // if automatic threshold is selected, empty list will be used.
                thresholds = new ArrayList<>(thresholdMap.values());
            }
        }

        Statistics statistics = new Statistics();
        Image thresholdImage = Core.performManualThresholding(image, thresholds, statistics);

        try {
            displayManualThresholdImageInNewWindowFxml(thresholdImage, statistics);
        } catch (IOException e) {
            displayFeedbackMessage("Chyba při zobrazování výsledku.");
        }
    }

    @FXML
    public void onLoadImageClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File f = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if (f != null) {
            Image image = new Image(f.toURI().toString());
            int w = (int) image.getWidth();
            int h = (int) image.getHeight();
            imageView.setImage(image);
            if(w < imageContainer.getWidth()) {
                imageView.setX((imageContainer.getWidth() - w ) / 2.0);
            } else {
                imageView.setX(0.0);
            }

            if(h < imageContainer.getHeight()) {
                imageView.setY((imageContainer.getHeight() - h) / 2.0);
            } else {
                imageView.setY(0.0);
            }
        }
    }

    @FXML
    public void performSegmentation() {
        Image image = imageView.getImage();
        if(image == null) {
            displayFeedbackMessage("Není načten žádný obrázek.");
            return;
        }
        if(image.getWidth() == 0 || image.getHeight() == 0) {
            displayFeedbackMessage("Špatné rozměry obrázku.");
            return;
        }

        Statistics statistics = new Statistics();
        float threshold = 0f;
        String thText = thresholdValLabel.getText();
        if(thText.isEmpty()) {
            displayFeedbackMessage("Není zadán threshold.");
            return;
        }
        try {
            threshold = Float.parseFloat(thText);
        } catch (NullPointerException ex) {
            displayFeedbackMessage("Není zadán threshold.");
            return;
        } catch (NumberFormatException ex) {
            displayFeedbackMessage(thText+ " není platné číslo.");
            return;
        }

        if(threshold < 0 || threshold > 255) {
            displayFeedbackMessage("Threshold musí být v rozmezí <0;255>!");
            return;
        }

        // segmentation, colorization
        // image is always colorized to grey first so that histogram can be computed
        segmentationProgress.setVisible(true);
        final float t = threshold;
        final boolean bright = brightColorsRadio.isSelected();
        segmentationThread = new Thread() {
            @Override
            public void run() {
                statistics.put(Statistics.PIXEL_COUNT, (int)(image.getHeight()*image.getWidth()));
                segmentationBtn.setDisable(true);
                Platform.runLater(() -> {
                    segmentationProgress.setProgress(0);
                    displayFeedbackMessage("Operace split...");
                });
                List<Region> regions = Core.performSplit(image, t, statistics);

                Platform.runLater(() -> {
                    segmentationProgress.setProgress(0.10);
                    displayFeedbackMessage("Sestavuji graf sousedů...");
                });
                Map<Region, List<Region>> neighbourGraph = Core.findNeighbours(regions);

                Platform.runLater(() -> {
                    segmentationProgress.setProgress(0.25);
                    displayFeedbackMessage("Operace merge...");
                });
                List<MergedRegion> mergedRegions = Core.performGrowing(regions, neighbourGraph, statistics);

                Platform.runLater(() -> {
                    segmentationProgress.setProgress(0.50);
                    displayFeedbackMessage("Zabarvení a histogram...");
                });
                Image greyColor = Core.colorize(image, mergedRegions, ColorizationMethod.AVERAGE);
                double[] histogram = Core.calculateHistogram(greyColor);
                Image segmentedImage = greyColor;
                if(bright) {
                    segmentedImage = Core.colorize(image, mergedRegions, ColorizationMethod.BRIGHT);
                }
                statistics.put(Statistics.HISTOGRAM, histogram);

                final Image img = segmentedImage;
                Platform.runLater(() -> {
                    segmentationProgress.setProgress(0.75);
                    displayFeedbackMessage("Zobrazení...");
                    try {
                        displaySegmentedImageInNewWindowFxml(img, statistics);
                        displayFeedbackMessage("Segmentace dokončena.");
                    } catch (IOException e) {
                        displayFeedbackMessage("Chyba při zobrazování");
                    } finally {
                        segmentationProgress.setProgress(0.100);
                        segmentationProgress.setVisible(false);
                        segmentationBtn.setDisable(false);
                        stopSegmentationBtn.setVisible(false);
                    }
                });
            }
        };
        stopSegmentationBtn.setVisible(true);
        segmentationThread.start();

        displayFeedbackMessage("Segmentace spuštěna");
    }

    public void onHistogramBtnClick() {
        Image image = imageView.getImage();
        if(image == null) {
            displayFeedbackMessage("Není žádný obrázek.");
            return;
        }
        if(image.getWidth() == 0 || image.getHeight() == 0) {
            displayFeedbackMessage("Špatné rozměry obrázku.");
            return;
        }

        double[] histogram = Core.calculateHistogram(image);
        Statistics statistics = new Statistics();
        statistics.put(Statistics.HISTOGRAM, histogram);
        try {
            displayHistogramInNewWindowFxml(statistics);
        } catch (IOException e) {
            displayFeedbackMessage("Chyba při zobrazování histogramu.");
            return;
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


    private void displaySegmentedImageInNewWindowFxml(Image segmentedImage, Statistics statistics) throws IOException {
        final Stage newWindow = new Stage();
        newWindow.initModality(Modality.NONE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("segmentedImgWindow.fxml"));
        Parent root = loader.load();

        newWindow.setTitle("Narůstání oblastí");
        newWindow.setScene(new Scene(root, 600,400));
        loader.<SegmentedImgWindowController>getController().init(segmentedImage, statistics);
        newWindow.show();
    }

    public void displayManualThresholdImageInNewWindowFxml(Image thresholdImage, Statistics statistics) throws IOException {
        final Stage newWindow = new Stage();
        newWindow.initModality(Modality.NONE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("thresholdImageWindow.fxml"));
        Parent root = loader.load();

        newWindow.setTitle("Ruční prahování");
        newWindow.setScene(new Scene(root, 600,400));
        loader.<ThresholdImageController>getController().init(thresholdImage, statistics);
        newWindow.show();
    }

    public void displayHistogramInNewWindowFxml(Statistics statistics) throws IOException {
        final Stage newWindow = new Stage();
        newWindow.initModality(Modality.NONE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("histogramWindow.fxml"));
        Parent root = loader.load();

        newWindow.setTitle("Histogram");
        newWindow.setScene(new Scene(root, 300,300));
        loader.<HistogramController>getController().init(statistics);
        newWindow.show();
    }

    private void displayFeedbackMessage(String message) {
        feedback.setText(message);
    }
}
