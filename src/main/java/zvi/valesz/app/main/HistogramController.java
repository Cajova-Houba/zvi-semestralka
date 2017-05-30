package zvi.valesz.app.main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import zvi.valesz.app.core.Statistics;

/**
 * Created by Zdenek Vales on 30.5.2017.
 */
public class HistogramController extends ControllerWithHistogram{

    @FXML
    private Canvas histogram;

    public void init(Statistics statistics) {
        double[] histogramData = (double[]) statistics.get(Statistics.HISTOGRAM);
        GraphicsContext gc = histogram.getGraphicsContext2D();
        drawHistogram(gc,histogramData,histogram.getHeight());
    }
}
