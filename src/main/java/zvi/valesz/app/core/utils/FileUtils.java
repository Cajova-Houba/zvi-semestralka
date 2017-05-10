package zvi.valesz.app.core.utils;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A simple library class.
 *
 * Created by valesz on 10.05.2017.
 */
public class FileUtils {

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

    /**
     * Loads a bmp image as an array of integers.
     * Note that array is in [height][width] format and contains RGB values.
     *
     *
     * @param file
     * @return
     */
    public static int[][] loadImage(File file) throws IOException {
        if(!file.exists()) {
            throw new FileNotFoundException(file.getPath());
        }

        int[][] res;
        BufferedImage bufferedImage = null;
        bufferedImage = ImageIO.read(file);

        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();

        res = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                res[i][j] = bufferedImage.getRGB(j,i);
            }
        }

        return res;
    }

    /**
     * Loads a bmp image as an array of integers.
     * Note that array is in [height][width] format and contains RGB values.
     *
     *
     * @param fileName
     * @return
     */
    public static int[][] loadImage(String fileName) throws IOException {
        File f = new File(fileName);
        return loadImage(f);
    }

    /**
     * Loads a bmp image as an array of integers.
     * Note that array is in [height][width] format and contain values in range 0-255.
     * If the source image is not in grey scale, only blue part of color will be used.
     *
     *
     * @param fileName
     * @return
     */
    public static int[][] loadGreyImage(String fileName) throws IOException {
        int[][] colorImage = loadImage(fileName);
        int blue = 0;
        int h = colorImage.length;
        int w = colorImage[0].length;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int rgb = colorImage[i][j];
                blue = rgb & 0x000000FF;
                colorImage[i][j] = blue;
            }
        }

        return colorImage;
    }
}
