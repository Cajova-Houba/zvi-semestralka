package zvi.valesz.app.main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import zvi.valesz.app.core.Statistics;

import java.util.Map;

/**
 * Controller for segmented image displayed in the new window.
 *
 * Created by Zdenek Vales on 28.5.2017.
 */
public class SegmentedImgWindowController {

    @FXML
    private Label threshold;

    @FXML
    private Label totalRegions;

    @FXML
    private Label mergedRegions;

    @FXML
    private ImageView segmentedImageView;

    @FXML
    private Canvas histogram;

    public void init(Image segmentedImage, Statistics statistics) {
        float t = (float)statistics.get(Statistics.THRESHOLD);
        int numOfRegions = (int)statistics.get(Statistics.MERGED_REGIONS_COUNT);
        int totalNumOfRegions = (int)statistics.get(Statistics.TOTAL_REGIONS_COUNT);

        threshold.setText(String.format("%.2f",t));
        totalRegions.setText(Integer.toString(totalNumOfRegions));
        mergedRegions.setText(Integer.toString(numOfRegions));

        histogram = new Canvas(256,255);
        int[] histogramData = (int[]) statistics.get(Statistics.HISTOGRAM);
        GraphicsContext gc = histogram.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,256,255);

        gc.setFill(Color.BLACK);
        for (int i = 0; i < histogramData.length; i++) {
            int val = histogramData[i];
            gc.fillRect(i,255,1,255-val);
        }

        segmentedImageView.setImage(segmentedImage);
    }
}
