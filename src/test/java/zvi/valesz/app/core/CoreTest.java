package zvi.valesz.app.core;

import org.junit.Test;
import zvi.valesz.app.core.region.MergedRegion;
import zvi.valesz.app.core.region.Region;
import zvi.valesz.app.core.utils.FileUtils;


import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
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

    /**
     * There are 5 regions in the image with colors:
     * 255, 150, 64, 48, 0
     *
     * @throws IOException
     */
    @Test
    public void testRegionGrowtFileThreshold2() throws IOException {
        String img1Name = "/threshold-test.bmp";
        int[][] image = FileUtils.loadGreyImage(getResource(img1Name));
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
