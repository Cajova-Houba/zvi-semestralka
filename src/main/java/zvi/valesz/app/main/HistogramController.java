package zvi.valesz.app.main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.stage.Window;
import zvi.valesz.app.core.Constants;
import zvi.valesz.app.core.Statistics;

/**
 * Created by Zdenek Vales on 30.5.2017.
 */
public class HistogramController extends BaseOutputController {

    @FXML
    private Canvas histogram;

    private Statistics statistics;

    public void saveHistogram() {
        Window owner = histogram.getScene().getWindow();
        statistics.put(Statistics.INPUT_IMAGE_FORMAT, Constants.BMP_EXT);
        WritableImage image = new WritableImage((int)histogram.getWidth(), (int)histogram.getHeight());
        histogram.snapshot(null, image);
        saveImage(image, statistics, owner);
    }

    public void init(Statistics statistics) {
        this.statistics = statistics;
        double[] histogramData = (double[]) statistics.get(Statistics.HISTOGRAM);
        GraphicsContext gc = histogram.getGraphicsContext2D();
        drawHistogram(gc,histogramData,histogram.getHeight());
    }
}
