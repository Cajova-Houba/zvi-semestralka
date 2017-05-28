package zvi.valesz.app.core.utils;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * A simple library class.
 *
 * Created by Zdenek Vales on 28.5.2017.
 */
public class ImageUtils {

    /**
     * Max value color can has (or one of the parts of color).
     */
    public static final int MAX_COLOR_VAL = 255;

    /**
     * Returns the grey color object created from value.
     * @param val Value in range from 0 to 255.
     * @return color.
     */
    public static Color getGreyColor(int val) {
        if (val < 0) {
            val = 0;
        }

        if (val > MAX_COLOR_VAL) {
            val = MAX_COLOR_VAL;
        }

        double v = val / (double)MAX_COLOR_VAL;
        return Color.color(v,v,v);
    }

    /**
     * Converts javafx image to array of integers from 0 to 255.
     * Average value of r-g-b is used.
     *
     * @param image
     * @return Array of ints in [height][width] format.
     */
    public static int[][] imageToGeryInt(Image image) {
        int w = (int)image.getWidth();
        int h = (int)image.getHeight();

        int[][] res = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = image.getPixelReader().getArgb(j,i);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                res[i][j] = (red+green+blue) / 3;
            }
        }

        return res;
    }
}
