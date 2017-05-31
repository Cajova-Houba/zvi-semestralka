package zvi.valesz.app.main;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import zvi.valesz.app.core.Statistics;
import zvi.valesz.app.core.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Base controller for output windows.
 *
 * Created by Zdenek Vales on 30.5.2017.
 */
public abstract class BaseOutputController {

    /**
     * Saves a string to txt file.
     * @param string
     * @param owner
     */
    protected void saveString(String string, Window owner) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Uložit data");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All", "*.*"),
                new FileChooser.ExtensionFilter("txt", "*.txt")
        );
        File f = fc.showSaveDialog(owner);
        if(f != null) {
            try {
                FileUtils.writeToFile(f, string);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chyba");
                alert.setHeaderText("Chyba při ukládání dat do souboru: "+f.getPath());
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                alert.setContentText(sw.toString());
                alert.show();
            }
        }
    }

    /**
     * Creates a dialog for saving image.
     * @param image Image to be saved.
     * @param statistics Input format expected in statistics.
     */
    protected void saveImage(Image image, Statistics statistics, Window owner) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Uložit obrázek");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All", "*.*"),
                new FileChooser.ExtensionFilter("jpg", "*.jpg"),
                new FileChooser.ExtensionFilter("png", "*.png"),
                new FileChooser.ExtensionFilter("bmp", "*.bmp")
        );
        File f = fc.showSaveDialog(owner);
        if(f != null) {
            FileChooser.ExtensionFilter ex = fc.getSelectedExtensionFilter();
            String ext = "";
            BufferedImage toBeSaved = SwingFXUtils.fromFXImage(image, null);
            if(ex.getDescription().equals("All")) {
                ext = (String) statistics.get(Statistics.INPUT_IMAGE_FORMAT);
                BufferedImage newBufferedImage = new BufferedImage((int)image.getWidth(),
                        (int)image.getHeight(), BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(toBeSaved, 0, 0, java.awt.Color.WHITE, null);
                toBeSaved = newBufferedImage;
            } else if (ex.getDescription().equals("jpg") || ex.getDescription().equals("bmp")){
                ext = ex.getDescription().toLowerCase();
                BufferedImage newBufferedImage = new BufferedImage((int)image.getWidth(),
                        (int)image.getHeight(), BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(toBeSaved, 0, 0, java.awt.Color.WHITE, null);
                toBeSaved = newBufferedImage;
            } else {
                ext = ex.getDescription();
            }
            try {
                ImageIO.write(toBeSaved, ext, f);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chyba");
                alert.setHeaderText("Chyba při ukládání obrázku do souboru: "+f.getPath());
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                alert.setContentText(sw.toString());
                alert.show();
            }
        }
    }

    protected void drawHistogram(GraphicsContext gc, double[] histogramData, double height) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,256,255);

        double max = Double.MIN_VALUE;
        for (int i = 0; i < histogramData.length; i++) {
            if(max < histogramData[i]) {
                max = histogramData[i];
            }
        }

        gc.setFill(Color.BLACK);
        for (int i = 0; i < histogramData.length; i++) {
            double val = histogramData[i]*(height/max);
            gc.fillRect(i,height-val,1,val);
        }
    }
}
