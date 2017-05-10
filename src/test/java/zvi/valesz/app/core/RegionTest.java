package zvi.valesz.app.core;

import org.junit.Test;
import zvi.valesz.app.core.region.MergedRegion;
import zvi.valesz.app.core.region.Region;

import java.util.*;

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

        List<Region> regions = Core.split(wholeImage);

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

    @Test
    public void testIsNeighbour() {
        int[][] image = new int[100][100];
        Region r1 = new Region(10,10,1,1,image);
        Region rightNeighbour = new Region(11,10,1,1,image);
        Region bottomNeighbour = new Region(10,11,1,1,image);
        Region leftneighbour = new Region(9,10,1,1,image);
        Region topNeighbour = new Region(10,9,1,1,image);

        assertTrue("Wrong right neighbour!",r1.isNeighbour(rightNeighbour));
        assertTrue("Wrong right neighbour!",rightNeighbour.isNeighbour(r1));

        assertTrue("Wrong left neighbour!",r1.isNeighbour(leftneighbour));
        assertTrue("Wrong left neighbour!",leftneighbour.isNeighbour(r1));

        assertTrue("Wrong bottom neighbour!",r1.isNeighbour(bottomNeighbour));
        assertTrue("Wrong bottom neighbour!",bottomNeighbour.isNeighbour(r1));

        assertTrue("Wrong top neighbour!",r1.isNeighbour(topNeighbour));
        assertTrue("Wrong top neighbour!",topNeighbour.isNeighbour(r1));
    }

    @Test
    public void testIsNeighbour2() {
        int[][] image = new int[100][100];
        Region r1 = new Region(10,10,1,1,image);
        Region topRight = new Region(11,9,1,1,image);
        Region bottomLeft = new Region(11,11,1,1,image);
        Region bottomRight = new Region(9,11,1,1,image);
        Region topLeft = new Region(9,11,1,1,image);

        assertFalse("Wrong right neighbour!",r1.isNeighbour(topRight));
        assertFalse("Wrong right neighbour!",topRight.isNeighbour(r1));

        assertFalse("Wrong left neighbour!",r1.isNeighbour(bottomLeft));
        assertFalse("Wrong left neighbour!",bottomLeft.isNeighbour(r1));

        assertFalse("Wrong bottom neighbour!",r1.isNeighbour(bottomRight));
        assertFalse("Wrong bottom neighbour!",bottomRight.isNeighbour(r1));

        assertFalse("Wrong top neighbour!",r1.isNeighbour(topLeft));
        assertFalse("Wrong top neighbour!",topLeft.isNeighbour(r1));
    }

    @Test
    public void testCanMerge() {
        int[][] image = new int[][] {
                new int[]{0,0,0,0},
                new int[]{0,1,1,0},
                new int[]{0,0,1,0},
                new int[]{0,0,1,0},
        };
        int w = 4;
        int h = 4;

        Region r0 = new Region(0,0,2,1,image);
        Region r1 = new Region(2,0,2,1,image);
        assertTrue("Mergable regions!", r0.canMerge(r1));
        assertTrue("Mergable regions!", r1.canMerge(r0));

        r1 = new Region(1,1,1,1,image);
        assertFalse("Non-mergable regions!", r0.canMerge(r1));
        assertFalse("Non-mergable regions!", r1.canMerge(r0));

        r0 = new Region(1,1,2,1,image);
        r1 = new Region(2,2,1,2,image);
        assertTrue("Mergable regions!", r0.canMerge(r1));
        assertTrue("Mergable regions!", r1.canMerge(r0));
    }

    @Test
    public void testMergeAlgorithm() {
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
    }

}
