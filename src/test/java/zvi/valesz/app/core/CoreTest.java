package zvi.valesz.app.core;

import org.junit.Ignore;
import org.junit.Test;

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

        List<List<Region>> mergedRegions = Core.merge(regions);

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

        List<List<Region>> mergedRegions = Core.merge(regions);

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

        List<List<Region>> mergedRegions = Core.merge(regions);

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

        List<List<Region>> mergedRegions = Core.merge(regions);

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

        List<List<Region>> mergedRegions = Core.merge(regions);

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

        List<List<Region>> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 5, mergedRegions.size());
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

        List<List<Region>> mergedRegions = Core.merge(regions);

        assertEquals("Wrong number of regions!", 3, mergedRegions.size());
    }
}
