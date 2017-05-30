package zvi.valesz.app.main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Controller with method for drawing a histogram.
 *
 * Created by Zdenek Vales on 30.5.2017.
 */
public abstract class ControllerWithHistogram {

    protected void drawHistogram(GraphicsContext gc, double[] histogramData, double height) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,256,255);

        gc.setFill(Color.BLACK);
        for (int i = 0; i < histogramData.length; i++) {
            double val = histogramData[i]*height;
            gc.fillRect(i,height-val,1,val);
        }
    }
}
