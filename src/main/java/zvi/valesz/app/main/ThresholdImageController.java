package zvi.valesz.app.main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import zvi.valesz.app.core.Statistics;

/**
 * Created by Zdenek Vales on 28.5.2017.
 */
public class ThresholdImageController extends ControllerWithHistogram{

    @FXML
    private Label imageSize;

    @FXML
    private Label thresholdCount;

    @FXML
    private ImageView thresholdImageView;

    @FXML
    private Canvas histogram;

    @FXML
    private Label autoThreshold;

    private Statistics statistics;

    public void init(Image thresholdImage, Statistics statistics) {
        this.statistics = statistics;
        int tCount = (int) statistics.get(Statistics.THRESHOLD_COUNT);
        double[] histogramData = (double[])statistics.get(Statistics.HISTOGRAM);
        int size = (int) statistics.get(Statistics.PIXEL_COUNT);
        if(statistics.containsKey(Statistics.AUTOMATIC_THRESHOLD)) {
            int aT = (int) statistics.get(Statistics.AUTOMATIC_THRESHOLD);
            autoThreshold.setText(Integer.toString(aT));
        } else {
            autoThreshold.setText("-");
        }
        GraphicsContext gc = histogram.getGraphicsContext2D();
        drawHistogram(gc,histogramData,histogram.getHeight());

        thresholdCount.setText(Integer.toString(tCount));
        imageSize.setText(Integer.toString(size));
        thresholdImageView.setImage(thresholdImage);
    }
}
