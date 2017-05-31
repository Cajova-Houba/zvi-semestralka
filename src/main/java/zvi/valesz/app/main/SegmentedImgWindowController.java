package zvi.valesz.app.main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Window;
import zvi.valesz.app.core.Constants;
import zvi.valesz.app.core.Statistics;

/**
 * Controller for segmented image displayed in the new window.
 *
 * Created by Zdenek Vales on 28.5.2017.
 */
public class SegmentedImgWindowController extends BaseOutputController {

    @FXML
    private Label threshold;

    @FXML
    private Label totalRegions;

    @FXML
    private Label mergedRegions;

    @FXML
    private Label imageSize;

    @FXML
    private ImageView segmentedImageView;

    @FXML
    private Canvas histogram;

    public void saveHistogram() {
        Window owner = histogram.getScene().getWindow();
        Statistics statistics = new Statistics();
        statistics.put(Statistics.INPUT_IMAGE_FORMAT, Constants.BMP_EXT);
        WritableImage image = new WritableImage((int)histogram.getWidth(), (int)histogram.getHeight());
        histogram.snapshot(null, image);
        saveImage(image, statistics, owner);
    }

    public void onSaveOutputClick() {
        Image image = segmentedImageView.getImage();
        Window owner = histogram.getScene().getWindow();
        Statistics statistics = new Statistics();
        statistics.put(Statistics.INPUT_IMAGE_FORMAT, Constants.BMP_EXT);
        saveImage(image, statistics, owner);
    }

    public void onSaveDataClick() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rozsah barev v oblasti: ").append(threshold.getText()).append("\n");
        sb.append("Počet regionů před merge: ").append(totalRegions.getText()).append("\n");
        sb.append("Počet regionů po merge: ").append(mergedRegions.getText()).append("\n");
        sb.append("Velikost obrázku (px): ").append(imageSize.getText()).append("\n");

        Window owner = threshold.getScene().getWindow();
        saveString(sb.toString(), owner);
    }

    public void init(Image segmentedImage, Statistics statistics) {
        float t = (float)statistics.get(Statistics.THRESHOLD);
        int numOfRegions = (int)statistics.get(Statistics.MERGED_REGIONS_COUNT);
        int totalNumOfRegions = (int)statistics.get(Statistics.TOTAL_REGIONS_COUNT);
        int imgSize = (int) statistics.get(Statistics.PIXEL_COUNT);

        threshold.setText(String.format("%.2f",t));
        totalRegions.setText(Integer.toString(totalNumOfRegions));
        mergedRegions.setText(Integer.toString(numOfRegions));
        imageSize.setText(Integer.toString(imgSize));

        double[] histogramData = (double[]) statistics.get(Statistics.HISTOGRAM);
        GraphicsContext gc = histogram.getGraphicsContext2D();
        drawHistogram(gc,histogramData,histogram.getHeight());

        segmentedImageView.setImage(segmentedImage);
    }
}
