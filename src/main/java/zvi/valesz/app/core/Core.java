package zvi.valesz.app.core;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import zvi.valesz.app.core.region.MergedRegion;
import zvi.valesz.app.core.region.Region;
import zvi.valesz.app.core.utils.ImageUtils;

import java.util.*;

/**
 * Core class for are core-reated operations.
 *
 * Created by Zdenek Vales on 6.5.2017.
 */
// todo: use threads
public class Core {

    /**
     * Possible colors to be used when colorizing regions in image.
     */
    public static final Color[] possibleColors = new Color[] {
            Color.CYAN,
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.PINK,
            Color.YELLOW,
            Color.ORANGE
    };

    /**
     * Performs a merge operation over a collection of homogene regions.
     * @param regions
     * @return Merged regions. Each list is one independent region in image.
     */
    public static List<MergedRegion> merge(List<Region> regions) {
        List<MergedRegion> mergedRegions = new ArrayList<>();
        int regCntr = 1;

        // merging
        while(!regions.isEmpty()) {
            Region r = regions.remove(0);
            MergedRegion mergedRegion = new MergedRegion(regCntr);
            regCntr++;
            List<Region> buffer = new LinkedList<>();
            buffer.add(r);


            // for every unmarked region of the list, go through it's neighbours
            // and check if they can be merged together
            while(!buffer.isEmpty()) {
                // take region from buffer
                Region bufferR = buffer.remove(0);
                Iterator<Region> regIt = regions.iterator();

                // go through remaining regions and add neighbours that can be merged to the buffer
                while(regIt.hasNext()) {
                    Region tmp = regIt.next();
                    if(bufferR.isNeighbour(tmp) && bufferR.canMerge(tmp)) {
                        buffer.add(tmp);
                        regIt.remove();
                    }
                }

                // add item from buffer to the result region list
                mergedRegion.add(bufferR);
            }

            // add merged region to result list
            mergedRegions.add(mergedRegion);
        }

        return mergedRegions;
    }

    /**
     * Performs a split operation over given Region object.
     * The region is splitted until only homogenic regions remain and those are returned.
     *
     * @param region
     * @return
     */
    public static List<Region> split(Region region) {
        List<Region> regionBuffer = new LinkedList<Region>();
        List<Region> regions = new ArrayList<>();

        // splitting
        regionBuffer.add(region);
        while(!regionBuffer.isEmpty()) {
            Region tmp = regionBuffer.remove(0);
            if(tmp.isHomogenic()) {
                regions.add(tmp);
            } else {
                for(Region r : tmp.split()) {
                    regionBuffer.add(r);
                }
            }
        }

        return regions;
    }

    /**
     * Colorizes regions in image except the region with the biggest regionSize (that one is assumed to be a background).
     *
     * @param image
     * @param regions
     * @return
     */
    public static Image colorize(Image image, List<MergedRegion> regions) {
        int maxSize = Integer.MIN_VALUE;
        int backgroundRegId = -1;

        for(MergedRegion mr : regions) {
            if(maxSize < mr.getRegionSize()) {
                backgroundRegId = mr.regionId;
                maxSize = mr.getRegionSize();
            }
        }

        // do colorization
        PixelReader imPixReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(
                imPixReader,
                (int)image.getWidth(),
                (int)image.getHeight()
        );
        PixelWriter imPixWriter = writableImage.getPixelWriter();
        int clrCntr = 0;
        for(MergedRegion mr : regions) {
//            if(mr.regionId != backgroundRegId) {
                Color c = possibleColors[clrCntr];
                int avg = mr.getAverageColor();
//                c = Color.color(avg/255.0, avg/255.0, avg/255.0);

                for(Region r : mr) {
                    for (int i = r.startY; i < r.startY+r.height; i++) {
                        for (int j = r.startX; j < r.startX+r.width; j++) {
                            imPixWriter.setColor(j,i,c);
                        }
                    }
                }

                clrCntr = (clrCntr + 1) % possibleColors.length;
            }
//        }


        return writableImage;
    }

    /**
     * Performs region growing over image and colorizes found regions.
     *
     * @param image Image to perform region growing over.
     * @param threshold Threshold to be used.
     * @param statistics Hash map to be filled with statistics data.
     * @return Image with colorized regions
     */
    public static List<MergedRegion> performRegionGrowing(Image image, float threshold, Map<String, Object> statistics) {
        // get int representation of image
        int[][] intImg = ImageUtils.imageToGeryInt(image);
        int h = intImg.length;
        int w = intImg[0].length;
        statistics.put(Statistics.THRESHOLD, threshold);

        // split into regions
        Region wholeImage = new Region(0,0,w,h,intImg, threshold);
        List<Region> regions = split(wholeImage);
        statistics.put(Statistics.TOTAL_REGIONS_COUNT, regions.size());

        // perform the merge
        List<MergedRegion> mergedRegions = merge(regions);
        statistics.put(Statistics.MERGED_REGIONS_COUNT, mergedRegions.size());

        return mergedRegions;
    }

    /**
     * Returns the threshold as (MIN+MAX)/2.
     *
     * @param image Source image.
     * @return Threshold.
     */
    public static int automaticThreshold(int[][] image) {
        int h = image.length;
        int w = image[0].length;
        int threshold = 0;

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if(image[i][j] < min) {
                    min = image[i][j];
                }

                if(image[i][j] > max) {
                    max = image[i][j];
                }
            }
        }

        threshold = (max+min) / 2;
        return threshold;
    }

    /**
     * Performs manual thresholding over the image represented by integer array.
     * If the threshold list is empty, original image is returned.
     * If some value in the image is greater than every threshold, 255 is used.
     *
     * @param image Image.
     * @param thresholds List of thresholds. Expected to have ascending order. If empty, automatic threshold will be used.
     * @param statistics Map containing statistics data.
     */
    public static Image performManualThresholding(Image image, List<Threshold> thresholds, Map<String, Object> statistics) {
        int[][] intImage = ImageUtils.imageToGeryInt(image);
        if(thresholds.isEmpty()) {
            int threshold = automaticThreshold(intImage);
            thresholds.add(new Threshold(threshold, 0));
            statistics.put(Statistics.AUTOMATIC_THRESHOLD, threshold);
        }
        int[][] thresholdImage = performManualThresholding(intImage, thresholds);
        int h = intImage.length;
        int w = intImage[0].length;
        int[] histogram = calculateHistogram(thresholdImage);


        statistics.put(Statistics.PIXEL_COUNT, w*h);
        statistics.put(Statistics.THRESHOLD_COUNT, thresholds.size());
        statistics.put(Statistics.HISTOGRAM, histogram);
        WritableImage writableImage = new WritableImage(w,h);
        PixelWriter imPixWriter = writableImage.getPixelWriter();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int val = thresholdImage[i][j];
                imPixWriter.setColor(j,i,ImageUtils.getGreyColor(val));
            }
        }

        return  writableImage;
    }

    /**
     * Performs manual thresholding over the image represented by integer array.
     * If the threshold list is empty, original image is returned.
     * If some value in the image is greater than every threshold, 255 is used.
     *
     * @param image Image, array is expected to be in [height][width].
     * @param thresholds List of thresholds. Expected to have ascending order.
     */
    public static int[][] performManualThresholding(int[][] image, List<Threshold> thresholds) {
        int h = image.length;
        int w = image[0].length;
        int[][] thresholdImage = new int[h][w];

        if(thresholds.isEmpty()) {
            return image;
        }

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                boolean value = false;
                for (Threshold t : thresholds) {
                    if(image[i][j] < t.threshold) {
                        thresholdImage[i][j] = t.newValue;
                        value = true;
                        break;
                    }
                }

                // no value assigned => use max
                if(!value) {
                    thresholdImage[i][j] = ImageUtils.MAX_COLOR_VAL;
                }
            }
        }

        return thresholdImage;
    }

    /**
     * Calculates a grey scale histogram (256 values)
     *
     * @param image Histogram of this image will be calculated.
     * @return Array of size 256 containing values from 0 to 255.
     */
    public static int[] calculateHistogram(Image image) {
        int[][] intImg = ImageUtils.imageToGeryInt(image);
        return calculateHistogram(intImg);
    }

    /**
     * Calculates a grey scale histogram (256 values)
     *
     * @param image Histogram of this image will be calculated. The array is expected to be in [height][width] format.
     *              Values lesser than 0 or greater than 255 will be trimmed to 0 or 255.
     * @return Array of size 256 containing values from 0 to 255.
     */
    public static int[] calculateHistogram(int[][] image) {
        int h = image.length;
        int w = image[0].length;
        int[] histogram = new int[256];

        // fill the histogram with 0s
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        // go through image and calculate the histogram
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int col = image[i][j];
                if(col < 0) {
                    histogram[0]++;
                } else if (col > 255) {
                    histogram[255]++;
                } else {
                    histogram[col]++;
                }
            }
        }

        return histogram;
    }
}
