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
     * Neighbour graph is used to decide if the two regions are neighbours or not.
     *
     * @param regions Regions to be merged.
     * @param neighbourGraph Neighbour graph of input regions.
     * @return
     */
    public static List<MergedRegion> merge(List<Region> regions, Map<Region, List<Region>> neighbourGraph) {
        List<MergedRegion> mergedRegions = new ArrayList<>();
        int regCntr = 1;
        List<Region> buffer = new LinkedList<>();
        List<Region> tmpRegions = new ArrayList<>(regions);

        // merging
        while(!tmpRegions.isEmpty()) {
            Region r = tmpRegions.remove(0);
            MergedRegion mergedRegion = new MergedRegion(regCntr);
            regCntr++;
            buffer.clear();
            buffer.add(r);


            // for every unmarked region of the list, go through it's neighbours
            // and check if they can be merged together
            while(!buffer.isEmpty()) {
                // take region from buffer
                Region bufferR = buffer.remove(0);

                // go through remaining regions and add neighbours that can be merged to the buffer
                List<Region> neighbours = neighbourGraph.get(bufferR);
                for(Region neighbour : neighbours) {
                    // neighbour must be in the region list also
                    if(bufferR.canMerge(neighbour) && tmpRegions.contains(neighbour)) {
                        buffer.add(neighbour);
                        tmpRegions.remove(neighbour);
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
     * Performs a merge operation over a collection of homogene regions.
     * @param regions
     * @return Merged regions. Each list is one independent region in image.
     */
    public static List<MergedRegion> merge(List<Region> regions) {
        Map<Region, List<Region>> neighbourGraph = findNeighbours(regions);
        return merge(regions, neighbourGraph);
//        List<MergedRegion> mergedRegions = new ArrayList<>();
//        int regCntr = 1;
//        List<Region> buffer = new LinkedList<>();
//
//        // merging
//        while(!regions.isEmpty()) {
//            Region r = regions.remove(0);
//            MergedRegion mergedRegion = new MergedRegion(regCntr);
//            regCntr++;
//            buffer.clear();
//            buffer.add(r);
//
//
//            // for every unmarked region of the list, go through it's neighbours
//            // and check if they can be merged together
//            while(!buffer.isEmpty()) {
//                // take region from buffer
//                Region bufferR = buffer.remove(0);
//                Iterator<Region> regIt = regions.iterator();
//
//                // go through remaining regions and add neighbours that can be merged to the buffer
//                while(regIt.hasNext()) {
//                    Region tmp = regIt.next();
//                    if(bufferR.isNeighbour(tmp) && bufferR.canMerge(tmp)) {
//                        buffer.add(tmp);
//                        regIt.remove();
//                    }
//                }
//
//                // add item from buffer to the result region list
//                mergedRegion.add(bufferR);
//            }
//
//            // add merged region to result list
//            mergedRegions.add(mergedRegion);
//        }
//
//        return mergedRegions;
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
    public static Image colorize(Image image, List<MergedRegion> regions, ColorizationMethod method) {
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
            Color c = possibleColors[clrCntr];
            if (method == ColorizationMethod.AVERAGE) {
                int avg = mr.getAverageColor();
                c = ImageUtils.getGreyColor(avg);
            }

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
     * Constructs a neighbour graph for regions.
     * @param regions
     * @return
     */
    public static Map<Region, List<Region>> findNeighbours(List<Region> regions) {
        Map<Region, List<Region>> neighbourGraph = new HashMap<>();

        // use temp list so the original one isn't modified.
        List<Region> tmpRegions = new ArrayList<>(regions);
        Iterator<Region> regIt = tmpRegions.iterator();
        while(regIt.hasNext()) {
            Region r = regIt.next();
            if(!neighbourGraph.containsKey(r)) {
                neighbourGraph.put(r, new ArrayList<>());
            }

            if(r.freeSlots > 0) {
                // free slots are points which can touch other regions

                // find neighbours of r
                for (Region tmp : tmpRegions) {
                    if(r.equals(tmp)) {
                        continue;
                    }

                    // neighbour
                    // add entry also for tmp
                    int adjPoints = r.calculateAdjacentPoints(tmp);
                    if(adjPoints > 0) {
                        neighbourGraph.get(r).add(tmp);
                        if(!neighbourGraph.containsKey(tmp)) {
                            neighbourGraph.put(tmp, new ArrayList<>());
                        }
                        neighbourGraph.get(tmp).add(r);
                        r.freeSlots -= adjPoints;
                        tmp.freeSlots -= adjPoints;
                    }
                }
            }

            regIt.remove();
        }

        return neighbourGraph;
    }

    /**
     * Converts image to greyscale, int[][] array and performs split operation.
     * This method is more suitable for usage with threads than performRegionGrowing.
     *
     * @param image Image to perform opeartion on.
     * @param threshold Threshold.
     * @param statistics Object for saving statistics data.
     * @return List of split regions.
     */
    public static List<Region> performSplit(Image image, float threshold, Statistics statistics) {
        int[][] intImg = ImageUtils.imageToGeryInt(image);
        int h = intImg.length;
        int w = intImg[0].length;
        statistics.put(Statistics.THRESHOLD, threshold);

        Region wholeImage = new Region(0,0,w,h,intImg, threshold);
        List<Region> regions = split(wholeImage);
        statistics.put(Statistics.TOTAL_REGIONS_COUNT, regions.size());

        return regions;
    }

    /**
     * Performs the growing operation over split regions.
     * @param splitRegions List of split regions to be merged.
     * @param statistics Object for saving statistics data.
     * @return List of merged regions.
     */
    public static List<MergedRegion> performGrowing(List<Region> splitRegions, Map<Region, List<Region>> neighbourGraph, Statistics statistics) {
        List<MergedRegion> mergedRegions = merge(splitRegions, neighbourGraph);
        statistics.put(Statistics.MERGED_REGIONS_COUNT, mergedRegions.size());

        return mergedRegions;
    }

    /**
     * Performs the growing operation over split regions.
     * @param splitRegions List of split regions to be merged.
     * @param statistics Object for saving statistics data.
     * @return List of merged regions.
     */
    public static List<MergedRegion> performGrowing(List<Region> splitRegions, Statistics statistics) {
        List<MergedRegion> mergedRegions = merge(splitRegions);
        statistics.put(Statistics.MERGED_REGIONS_COUNT, mergedRegions.size());

        return mergedRegions;
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
        double[] histogram = calculateHistogram(thresholdImage);


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
     * @return Array of size 256 containing values from 0 to 1.
     */
    public static double[] calculateHistogram(Image image) {
        int[][] intImg = ImageUtils.imageToGeryInt(image);
        return calculateHistogram(intImg);
    }

    /**
     * Calculates a grey scale histogram (256 values)
     *
     * @param image Histogram of this image will be calculated. The array is expected to be in [height][width] format.
     *              Values lesser than 0 or greater than 255 will be trimmed to 0 or 255.
     * @return Array of size 256 containing values from 0 to 1.
     */
    public static double[] calculateHistogram(int[][] image) {
        int h = image.length;
        int w = image[0].length;
        double[] histogram = new double[256];

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

        // normalize the values
        double size = w*h;
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] /= size;
        }

        return histogram;
    }
}
