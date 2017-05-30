package zvi.valesz.app.core;

import javafx.scene.image.Image;
import org.junit.Test;
import zvi.valesz.app.core.region.MergedRegion;
import zvi.valesz.app.core.region.Region;
import zvi.valesz.app.core.utils.FileUtils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Zdenek Vales on 6.5.2017.
 */
public class CoreTest {

    @Test
    public void testRegionGrowing1() {
        int[][] image = new int[][] {
                new int[]{0,0,0,0},
                new int[]{0,1,1,0},
                new int[]{0,0,1,0},
                new int[]{0,0,1,0},
        };
        int w = 4;
        int h = 4;

        Region wholeImage = new Region(0,0,w,h,image);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 2, mergedRegions.size());
        for(List<Region> mergedRegion : mergedRegions) {
            for(Region r : mergedRegion) {
                assertTrue("Region should be homogenic!", r.isHomogenic());
            }
        }
    }

    @Test
    public void testRegionGrowing2() {
        int[][] image = new int[][] {
                new int[]{0,0,0},
                new int[]{0,1,0},
                new int[]{0,0,0},
        };
        int w = 3;
        int h = 3;

        Region wholeImage = new Region(0,0,w,h,image);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 2, mergedRegions.size());
    }

    @Test
    public void testRegionGrowing3() {
        int[][] image = new int[][] {
                new int[]{1,1,1,1,1},
                new int[]{1,0,0,0,1},
                new int[]{1,0,1,0,1},
                new int[]{1,0,0,0,1},
                new int[]{1,1,1,1,1},
        };
        int w = 5;
        int h = 5;

        Region wholeImage = new Region(0,0,w,h,image);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 3, mergedRegions.size());
    }

    @Test
    public void testRegionGrowing4() {
        int[][] image = new int[][] {
                new int[]{1,1,0,1,1},
                new int[]{1,0,0,0,1},
                new int[]{1,0,1,0,1},
                new int[]{1,0,0,0,1},
                new int[]{1,1,0,1,1},
        };
        int w = 5;
        int h = 5;

        Region wholeImage = new Region(0,0,w,h,image);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 4, mergedRegions.size());
    }

    @Test
    public void testRegionGrowing5() {
        int[][] image = new int[][] {
                new int[]{1,0,1,0,1},
        };
        int w = 5;
        int h = 1;

        Region wholeImage = new Region(0,0,w,h,image);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 5, mergedRegions.size());
    }

    @Test
    public void testRegionGrowing6() {
        int[][] image = new int[][] {
                new int[]{1},
                new int[]{0},
                new int[]{1},
                new int[]{0},
                new int[]{1},
        };
        int w = 1;
        int h = 5;

        Region wholeImage = new Region(0,0,w,h,image);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 5, mergedRegions.size());
    }

    @Test
    public void testRegionGrowing7() {
        int[][] image = new int[][] {
                new int[]{2,2,0,2,2},
                new int[]{2,0,0,0,2},
                new int[]{0,0,3,3,0},
                new int[]{2,3,3,3,2},
                new int[]{2,2,0,2,2},
        };
        int w = 5;
        int h = 5;

        Region wholeImage = new Region(0,0,w,h,image);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 8, mergedRegions.size());
//        testPrintRegions(mergedRegions, w, h);
    }

    @Test
    public void testRegionGrowWithThreshold1() {
        int[][] image = new int[][] {
                new int[]{2,2,3,2,2},
                new int[]{2,0,0,0,2},
                new int[]{2,0,2,0,2},
                new int[]{2,0,0,0,2},
                new int[]{2,2,3,2,2},
        };
        int w = 5;
        int h = 5;
        float threshold = 1;

        Region wholeImage = new Region(0,0,w,h,image, threshold);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 3, mergedRegions.size());
    }

    @Test
    public void testRegionGrowWithThreshold2() {
        int[][] image = new int[][] {
                new int[]{2,2,3,2,2,3},
                new int[]{2,0,0,0,2,3},
                new int[]{2,0,2,0,2,0},
                new int[]{2,0,0,0,2,0},
                new int[]{2,2,3,2,2,0},
        };
        int w = 6;
        int h = 5;
        float threshold = 1;

        Region wholeImage = new Region(0,0,w,h,image, threshold);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 4, mergedRegions.size());

        // test print
//        testPrintRegions(mergedRegions, w, h);
    }

    @Test
    public void testRegionGrowtFile() throws IOException {
        String img1Name = "/test-img1.bmp";
        int[][] image = FileUtils.loadImage(getResource(img1Name));
        int w = image[0].length;
        int h = image.length;
        float threshold = 1;

        Region wholeImage = new Region(0,0,w,h,image, threshold);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 5, mergedRegions.size());

        // test print
//        testPrintRegions(mergedRegions, w, h);
    }

    @Test
    public void testRegionGrowtFile2() throws IOException {
        String img1Name = "/test-img2.bmp";
        int[][] image = FileUtils.loadImage(getResource(img1Name));
        int w = image[0].length;
        int h = image.length;
        float threshold = 1;

        Region wholeImage = new Region(0,0,w,h,image, threshold);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 4, mergedRegions.size());

        // test print
//        testPrintRegions(mergedRegions, w, h);
    }

    @Test
    public void testRegionGrowtFileThreshold() throws IOException {
        String img1Name = "/test-img3.bmp";
        int[][] image = FileUtils.loadGreyImage(getResource(img1Name));
        int w = image[0].length;
        int h = image.length;
        float threshold = 127;

        Region wholeImage = new Region(0,0,w,h,image, threshold);

        List<Region> regions = Core.split(wholeImage);

        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 3, mergedRegions.size());

        // test print
        testPrintRegions(mergedRegions, w, h);
    }

    @Test
    public void testRegionGrowingFile4() throws IOException {
        String imgName = "/test-img4.bmp";
        int[][] image = FileUtils.loadGreyImage(getResource(imgName));
        int w = image[0].length;
        int h = image.length;
        int expRegions = 10;

        Region wholeImage = new Region(0,0,w,h,image, 0f);
        List<Region> regions = Core.split(wholeImage);
        List<MergedRegion> mergedRegions = Core.merge(regions);

        assertNotNull("Null returned!", mergedRegions);
        assertEquals("Wrong number of merged regions!", expRegions, mergedRegions.size());
    }

    /**
     * There are 5 regions in the image with colors:
     * 255, 150, 64, 48, 0
     *
     * @throws IOException
     */
    @Test
    public void testRegionGrowtFileThreshold2() throws IOException {
        String imgName = "/threshold-test.bmp";
        int[][] image = FileUtils.loadGreyImage(getResource(imgName));
        int w = image[0].length;
        int h = image.length;

        // 0 threshold
        float threshold = 0;
        Region wholeImage = new Region(0,0,w,h,image, threshold);
        List<Region> regions = Core.split(wholeImage);
        List<MergedRegion> mergedRegions = Core.merge(regions);
        assertEquals("Wrong number of regions for threshold "+threshold+"!", 5, mergedRegions.size());

        // 16 threshold
        threshold = 16;
        wholeImage = new Region(0,0,w,h,image, threshold);
        regions = Core.split(wholeImage);
        mergedRegions = Core.merge(regions);
        assertEquals("Wrong number of regions for threshold "+threshold+"!", 4, mergedRegions.size());

        // 48 threshold
        threshold = 48;
        wholeImage = new Region(0,0,w,h,image, threshold);
        regions = Core.split(wholeImage);
        mergedRegions = Core.merge(regions);
        assertEquals("Wrong number of regions for threshold "+threshold+"!", 3, mergedRegions.size());

        // 86 threshold
        threshold = 86;
        wholeImage = new Region(0,0,w,h,image, threshold);
        regions = Core.split(wholeImage);
        mergedRegions = Core.merge(regions);
        assertEquals("Wrong number of regions for threshold "+threshold+"!", 2, mergedRegions.size());

        // 105 threshold
        threshold = 105;
        wholeImage = new Region(0,0,w,h,image, threshold);
        regions = Core.split(wholeImage);
        mergedRegions = Core.merge(regions);
        assertEquals("Wrong number of regions for threshold "+threshold+"!", 1, mergedRegions.size());


        // test print
//        testPrintRegions(mergedRegions, w, h);
    }

    @Test
    public void testCalculateHistogram() {
        int[][] image = new int[][] {
                new int[]{2,2,3,2,2,3},
                new int[]{2,0,0,-1,2,3},
                new int[]{2,0,2,0,2,0},
                new int[]{2,0,0,0,2,0},
                new int[]{2,2,3,2,2,256},
        };
        int[] histogram = Core.calculateHistogram(image);
        int expSize = 256;
        int zeros = 10;
        int ones = 0;
        int twos = 15;
        int threes = 4;
        int tooBigs = 1;

        assertEquals("Wrong size of the histogram!",expSize, histogram.length);
        assertEquals("Wrong number of zeros in histogram!", zeros, histogram[0]);
        assertEquals("Wrong number of ones in histogram!", ones, histogram[1]);
        assertEquals("Wrong number of twos in histogram!", twos, histogram[2]);
        assertEquals("Wrong number of threes in histogram!", threes, histogram[3]);
        assertEquals("Wrong number of 255s in histogram!", tooBigs, histogram[255]);
        for (int i = 4; i < 255; i++) {
            assertEquals("Wrong number of "+i+" in histogram!", 0, histogram[i]);
        }
    }

    @Test
    public void testManualThresholding() {
        int[][] image = new int[][] {
                new int[]{0,5,2,3,2},
                new int[]{0,5,0,5,1},
                new int[]{3,4,10,0,5},
                new int[]{0,5,5,1,2},
                new int[]{2,1,3,0,3},
        };
        int w = 5;
        int h = 5;
        int threshold = 5;
        int[][] thresholdImage = new int[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if(image[i][j] > threshold) {
                    thresholdImage[i][j] = 10;
                } else {
                    thresholdImage[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if(i == 2 && j == 2) {
                    assertEquals("High value expected for ("+j+","+i+")!",10,thresholdImage[i][j]);
                } else {
                    assertEquals("Low value expected for ("+j+","+i+")!", 0, thresholdImage[i][j]);
                }
            }
        }
    }

    @Test
    public void testManualThresholding2() {
        int[][] image = new int[][] {
                new int[]{0,0,2,1,0,2},
                new int[]{1,3,6,5,4,1},
                new int[]{2,4,7,9,3,0},
                new int[]{0,5,8,9,6,2},
                new int[]{1,6,3,4,5,0},
                new int[]{2,0,1,2,0,1},
        };
        int[][] expectedImage = new int[][] {
                new int[]{0,0,0,0,0,0},
                new int[]{0,5,5,5,5,0},
                new int[]{0,5,9,9,5,0},
                new int[]{0,5,9,9,5,0},
                new int[]{0,5,5,5,5,0},
                new int[]{0,0,0,0,0,0},
        };
        int w = 6;
        int h = 6;
        List<Threshold> thresholds = new ArrayList<>();
        thresholds.add(new Threshold(3,0));
        thresholds.add(new Threshold(7,5));
        thresholds.add(new Threshold(11,9));

        int[][] thresholdImage = Core.performManualThresholding(image, thresholds);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                assertEquals("Wrong value on ("+j+","+i+")!", expectedImage[i][j], thresholdImage[i][j]);
            }
        }
    }

    /**
     * Test with empty threshold list.
     */
    @Test
    public void testManualThresholding3() {
        int[][] image = new int[][] {
                new int[]{0,0,2,1,0,2},
                new int[]{1,3,6,5,4,1},
                new int[]{2,4,7,9,3,0},
                new int[]{0,5,8,9,6,2},
                new int[]{1,6,3,4,5,0},
                new int[]{2,0,1,2,0,1},
        };
        int[][] expectedImage = new int[][] {
                new int[]{0,0,2,1,0,2},
                new int[]{1,3,6,5,4,1},
                new int[]{2,4,7,9,3,0},
                new int[]{0,5,8,9,6,2},
                new int[]{1,6,3,4,5,0},
                new int[]{2,0,1,2,0,1},
        };
        int w = 6;
        int h = 6;
        List<Threshold> thresholds = new ArrayList<>();

        int[][] thresholdImage = Core.performManualThresholding(image, thresholds);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                assertEquals("Wrong value on ("+j+","+i+")!", expectedImage[i][j], thresholdImage[i][j]);
            }
        }
    }

    /**
     * Test with just one threshold.
     */
    @Test
    public void testManualThresholding4() {
        int[][] image = new int[][] {
                new int[]{0,0,2,1,0,2},
                new int[]{1,3,6,5,4,1},
                new int[]{2,4,7,9,3,0},
                new int[]{0,5,8,9,6,2},
                new int[]{1,6,3,4,5,0},
                new int[]{2,0,1,2,0,1},
        };
        int[][] expectedImage = new int[][] {
                new int[]{0,0,0,0,0,0},
                new int[]{0,0,255,255,0,0},
                new int[]{0,0,255,255,0,0},
                new int[]{0,255,255,255,255,0},
                new int[]{0,255,0,0,255,0},
                new int[]{0,0,0,0,0,0},
        };
        int w = 6;
        int h = 6;
        List<Threshold> thresholds = new ArrayList<>();
        thresholds.add(new Threshold(5,0));

        int[][] thresholdImage = Core.performManualThresholding(image, thresholds);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                assertEquals("Wrong value on ("+j+","+i+")!", expectedImage[i][j], thresholdImage[i][j]);
            }
        }
    }

    @Test
    public void testAutomaticThreshold() {
        int[][] image = new int[][] {
                new int[]{0,0,2,1,0,2},
                new int[]{1,3,6,5,4,1},
                new int[]{2,4,7,9,3,0},
                new int[]{0,5,8,9,6,2},
                new int[]{1,6,3,4,5,0},
                new int[]{2,0,1,2,0,1},
        };
        int expThreshold = 9 / 2;

        int threshold = Core.automaticThreshold(image);
        assertEquals("Wrong threshold!", expThreshold, threshold);
    }

    private String getResource(String resourceName) {
        return getClass().getResource(resourceName).getPath();
    }

    private void testPrintRegions(List<MergedRegion> mergedRegions, int w, int h) {
        int[][] regionDisplay = new int[h][w];
        for(MergedRegion mergedRegion : mergedRegions) {
            for(Region r : mergedRegion) {
                for(int i = r.startY; i < r.startY+r.height; i++) {
                    for(int j = r.startX; j < r.startX+r.width; j++){
                        regionDisplay[i][j] = mergedRegion.regionId;
                    }
                }
            }
        }
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                System.out.print(regionDisplay[i][j]+" ");
            }
            System.out.print("\n");
        }
    }


}
