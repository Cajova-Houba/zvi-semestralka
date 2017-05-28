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
