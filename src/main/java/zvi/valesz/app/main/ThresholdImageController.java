package zvi.valesz.app.main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import zvi.valesz.app.core.Statistics;

/**
 * Created by Zdenek Vales on 28.5.2017.
 */
public class ThresholdImageController {

    @FXML
    private Label imageSize;

    @FXML
    private Label thresholdCount;

    @FXML
    private ImageView thresholdImageView;

    @FXML
    private Canvas histogram;

    public void init(Image thresholdImage, Statistics statistics) {
        int tCount = (int) statistics.get(Statistics.THRESHOLD_COUNT);
        int[] histogramData = (int[])statistics.get(Statistics.HISTOGRAM);
        int size = (int) statistics.get(Statistics.PIXEL_COUNT);

        thresholdImageView.setImage(thresholdImage);
    }
}
