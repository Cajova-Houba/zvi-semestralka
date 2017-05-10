package zvi.valesz.app.core;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import zvi.valesz.app.core.region.MergedRegion;
import zvi.valesz.app.core.region.Region;
import zvi.valesz.app.core.utils.FileUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
            if(mr.regionId != backgroundRegId) {
                Color c = possibleColors[clrCntr];

                for(Region r : mr) {
                    for (int i = r.startY; i < r.startY+r.height; i++) {
                        for (int j = r.startX; j < r.startX+r.width; j++) {
                            imPixWriter.setColor(j,i,c);
                        }
                    }
                }

                clrCntr = (clrCntr + 1) % possibleColors.length;
            }
        }


        return writableImage;
    }

    /**
     * Performs region growing over image and colorizes found regions.
     *
     * @param image Image to perform region growing over.
     * @return Image with colorized regions
     */
    public static List<MergedRegion> performRegionGrowing(Image image, float threshold) {
        // get int representation of image
        int[][] intImg = FileUtils.imageToGeryInt(image);
        int h = intImg.length;
        int w = intImg[0].length;
        Region wholeImage = new Region(0,0,w,h,intImg, threshold);
        List<Region> regions = split(wholeImage);
        List<MergedRegion> mergedRegions = merge(regions);

        return mergedRegions;
    }
}
