package zvi.valesz.app.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zdenek Vales on 24.4.2017.
 */
// todo
public class RegionTest {

    @Test
    public void testSplit() {
        int[][] image = new int[][] {
                new int[]{0,0,0,0},
                new int[]{0,1,1,0},
                new int[]{0,0,1,0},
                new int[]{0,0,1,0},
        };
        int w = 4;
        int h = 4;

        Region wholeImage = new Region(0,0,w,h,image);
        assertFalse("Region doesn't have same color!", wholeImage.isHomogenic());

        Region[] split = wholeImage.split();
        assertNotNull("Null returned!", split);
        assertEquals("4 images expected!", 4, split.length);

        // check regions
        Region r = split[0];
        assertEquals("Wrong start x of r1!", 0, r.startX);
        assertEquals("Wrong start y of r1!", 0, r.startY);
        assertEquals("Wrong width of r1!", 2, r.width);
        assertEquals("Wrong height of r1!", 2, r.height);

        r = split[1];
        assertEquals("Wrong start x of r2!", 2, r.startX);
        assertEquals("Wrong start y of r2!", 0, r.startY);
        assertEquals("Wrong width of r2!", 2, r.width);
        assertEquals("Wrong height of r2!", 2, r.height);

        r = split[2];
        assertEquals("Wrong start x of r3!", 0, r.startX);
        assertEquals("Wrong start y of r3!", 2, r.startY);
        assertEquals("Wrong width of r3!", 2, r.width);
        assertEquals("Wrong height of r3!", 2, r.height);

        r = split[3];
        assertEquals("Wrong start x of r4!", 2, r.startX);
        assertEquals("Wrong start y of r4!", 2, r.startY);
        assertEquals("Wrong width of r4!", 2, r.width);
        assertEquals("Wrong height of r4!", 2, r.height);
    }

    @Test
    public void testSplit2() {
        int[][] image = new int[][] {
                new int[]{0,0,0,0},
                new int[]{0,1,1,0},
                new int[]{0,0,1,0},
                new int[]{0,0,1,0},
                new int[]{0,0,0,0},
        };
        int w = 4;
        int h = 5;

        Region wholeImage = new Region(0,0,w,h,image);
        assertFalse("Region doesn't have same color!", wholeImage.isHomogenic());

        Region[] split = wholeImage.split();
        assertNotNull("Null returned!", split);
        assertEquals("4 images expected!", 4, split.length);

        // check regions
        Region r = split[0];
        assertEquals("Wrong start x of r1!", 0, r.startX);
        assertEquals("Wrong start y of r1!", 0, r.startY);
        assertEquals("Wrong width of r1!", 2, r.width);
        assertEquals("Wrong height of r1!", 2, r.height);

        r = split[1];
        assertEquals("Wrong start x of r2!", 2, r.startX);
        assertEquals("Wrong start y of r2!", 0, r.startY);
        assertEquals("Wrong width of r2!", 2, r.width);
        assertEquals("Wrong height of r2!", 2, r.height);

        r = split[2];
        assertEquals("Wrong start x of r3!", 0, r.startX);
        assertEquals("Wrong start y of r3!", 2, r.startY);
        assertEquals("Wrong width of r3!", 2, r.width);
        assertEquals("Wrong height of r3!", 3, r.height);

        r = split[3];
        assertEquals("Wrong start x of r4!", 2, r.startX);
        assertEquals("Wrong start y of r4!", 2, r.startY);
        assertEquals("Wrong width of r4!", 2, r.width);
        assertEquals("Wrong height of r4!", 3, r.height);
    }

    @Test
    public void testSplit3() {
        int[][] image = new int[][] {
                new int[]{0,0,0,0,0},
                new int[]{0,1,1,0,0},
                new int[]{0,0,1,0,0},
                new int[]{0,0,1,0,0},
        };
        int w = 5;
        int h = 4;

        Region wholeImage = new Region(0,0,w,h,image);
        assertFalse("Region doesn't have same color!", wholeImage.isHomogenic());

        Region[] split = wholeImage.split();
        assertNotNull("Null returned!", split);
        assertEquals("4 images expected!", 4, split.length);

        // check regions
        Region r = split[0];
        assertEquals("Wrong start x of r1!", 0, r.startX);
        assertEquals("Wrong start y of r1!", 0, r.startY);
        assertEquals("Wrong width of r1!", 2, r.width);
        assertEquals("Wrong height of r1!", 2, r.height);

        r = split[1];
        assertEquals("Wrong start x of r2!", 2, r.startX);
        assertEquals("Wrong start y of r2!", 0, r.startY);
        assertEquals("Wrong width of r2!", 3, r.width);
        assertEquals("Wrong height of r2!", 2, r.height);

        r = split[2];
        assertEquals("Wrong start x of r3!", 0, r.startX);
        assertEquals("Wrong start y of r3!", 2, r.startY);
        assertEquals("Wrong width of r3!", 2, r.width);
        assertEquals("Wrong height of r3!", 2, r.height);

        r = split[3];
        assertEquals("Wrong start x of r4!", 2, r.startX);
        assertEquals("Wrong start y of r4!", 2, r.startY);
        assertEquals("Wrong width of r4!", 3, r.width);
        assertEquals("Wrong height of r4!", 2, r.height);
    }

    @Test
    public void testSplit4() {
        int[][] image = new int[][] {
                new int[]{0,0,0,0,1},
                new int[]{0,1,1,0,0},
                new int[]{0,0,1,0,0},
                new int[]{0,0,1,0,1},
                new int[]{0,0,0,0,0},
        };
        int w = 5;
        int h = 5;

        Region wholeImage = new Region(0,0,w,h,image);
        assertFalse("Region doesn't have same color!", wholeImage.isHomogenic());

        Region[] split = wholeImage.split();
        assertNotNull("Null returned!", split);
        assertEquals("4 images expected!", 4, split.length);

        // check regions
        Region r = split[0];
        assertEquals("Wrong start x of r1!", 0, r.startX);
        assertEquals("Wrong start y of r1!", 0, r.startY);
        assertEquals("Wrong width of r1!", 2, r.width);
        assertEquals("Wrong height of r1!", 2, r.height);

        r = split[1];
        assertEquals("Wrong start x of r2!", 2, r.startX);
        assertEquals("Wrong start y of r2!", 0, r.startY);
        assertEquals("Wrong width of r2!", 3, r.width);
        assertEquals("Wrong height of r2!", 2, r.height);

        r = split[2];
        assertEquals("Wrong start x of r3!", 0, r.startX);
        assertEquals("Wrong start y of r3!", 2, r.startY);
        assertEquals("Wrong width of r3!", 2, r.width);
        assertEquals("Wrong height of r3!", 3, r.height);

        r = split[3];
        assertEquals("Wrong start x of r4!", 2, r.startX);
        assertEquals("Wrong start y of r4!", 2, r.startY);
        assertEquals("Wrong width of r4!", 3, r.width);
        assertEquals("Wrong height of r4!", 3, r.height);
    }

    @Test
    public void testSplittingAlgorithm() {
        int[][] image = new int[][] {
                new int[]{0,0,0,0},
                new int[]{0,1,1,0},
                new int[]{0,0,1,0},
                new int[]{0,0,1,0},
        };
        int w = 4;
        int h = 4;

        Region wholeImage = new Region(0,0,w,h,image);
        List<Region> regionBuffer = new LinkedList<Region>();
        List<Region> regions = new ArrayList<>();
        regionBuffer.add(wholeImage);

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

        assertEquals("Wrong number of regions!", 13, regions.size());
        for(Region r : regions) {
            assertTrue("Region is not homogenic!", r.isHomogenic());
        }
    }

    @Test
    public void testSplittingAlgorithm2() {
        int[][] image = new int[][] {
                new int[]{0,0,0,0,0},
                new int[]{0,0,1,1,0},
                new int[]{0,0,1,0,0},
                new int[]{0,0,1,0,0},
        };
        int w = 5;
        int h = 4;

        Region wholeImage = new Region(0,0,w,h,image);
        List<Region> regionBuffer = new LinkedList<Region>();
        List<Region> regions = new ArrayList<>();
        regionBuffer.add(wholeImage);

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

        assertEquals("Wrong number of regions!", 11, regions.size());
        for(Region r : regions) {
            assertTrue("Region is not homogenic!", r.isHomogenic());
        }
    }
}
