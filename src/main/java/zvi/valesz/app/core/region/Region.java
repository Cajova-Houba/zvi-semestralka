package zvi.valesz.app.core.region;

import zvi.valesz.app.core.Constants;

/**
 * A region in image.
 *
 * Created by Zdenek Vales on 24.4.2017.
 */
// todo: constant for 4-neighbours or 8-neighbours - 4 neighbours are used currently
public class Region {

    public static final float DEFAULT_THRESHOLD = 0.5f;

    public final int startX;
    public final int startY;
    public final int width;
    public final int height;
    public boolean marked;
    public final int[][] image;

    public final float threshold;

    /**
     * Whether 4 or 8 neighbours should be used.
     */
    public final int neighbours = Constants.NEIGHBOURS_4;

    /**
     * Perimeter of this region.
     */
    public final int perimeter;

    /**
     * Number of free slots for region to have neighbours.
     */
    public int freeSlots;

    /**
     * Minimum value of this region.
     */
    public int minVal;

    /**
     * Maximum value of this region.
     */
    public int maxVal;

    /**
     * Creates new region with specified threshold.
     *
     * @param startX X coordinate of top left corner of this region in image.
     * @param startY Y coordinate of top left corner of this region in image.
     * @param width Width of this region.
     * @param height Height of this region.
     * @param image "Pointer" to the whole image.
     */
    public Region(int startX, int startY, int width, int height, int[][] image, float threshold) {
        marked = false;
        if(startX < 0 || startY < 0) {
            throw new IllegalArgumentException("Wrong start position!");
        }

        if(width <=0 || height <= 0) {
            throw new IllegalArgumentException("Wrong dimensions!");
        }

        if(image[0].length <= 0 || image[0].length < (startX+width) || image.length <= 0 || image.length < (startY+height)) {
            throw new IllegalArgumentException("Wrong image!");
        }

        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.image = image;
        this.threshold = threshold;
        this.perimeter = 2*width + 2*height;

        calculateMinMax();
        calculateFreeSlots();
    }

    /**
     * Creates new region.
     *
     * @param startX X coordinate of top left corner of this region in image.
     * @param startY Y coordinate of top left corner of this region in image.
     * @param width Width of this region.
     * @param height Height of this region.
     * @param image "Pointer" to the whole image.
     */
    public Region(int startX, int startY, int width, int height, int[][] image) {
        this(startX, startY, width, height, image, DEFAULT_THRESHOLD);
    }

    private void calculateFreeSlots() {
        // number of free slots on each side
        int r = 0, l = 0, t = 0, b = 0;

        // region isn't on the left edge of the image
        if(startX > 0) {
            l = height;
        }

        // region isn't on the right edge of the image
        if((startX+width) < image[0].length) {
            r = height;
        }

        // region isn't on the top edge of the image
        if(startY > 0) {
            t = width;
        }

        // region isn't on the bottom edge of the image
        if((startY+height) < image.length) {
            b = width;
        }

        freeSlots = r+l+t+b;
    }

    private void calculateMinMax() {
        int minTmp = Integer.MAX_VALUE;
        int maxTmp = Integer.MIN_VALUE;

        for(int i = startY; i < startY+height; i++) {
            for (int j = startX; j <startX+width; j++) {
                if(minTmp > image[i][j]) {
                    minTmp = image[i][j];
                }

                if(maxTmp < image[i][j]) {
                    maxTmp = image[i][j];
                }
            }
        }

        maxVal = maxTmp;
        minVal = minTmp;
    }

    /**
     * Number of adjacent points of this region and other regions.
     * @param region Other region.
     * @return
     */
    public int calculateAdjacentPoints(Region region) {
        int adjacentPoints = 0;

        boolean topBottom = (
                (startX <= region.startX && region.startX < startX+width) ||
                        (region.startX <= startX && startX < region.startX+region.width)) && (
                (startY - region.startY == region.height)
                        || (region.startY - startY == height)
        );
        boolean leftRight = (
                (startY <= region.startY && region.startY < startY+height) ||
                        (region.startY <= startY && startY < region.startY+region.height)) && (
                (startX - region.startX == region.width)
                        || (region.startX - startX == width)
        );

        // regions are touching on topBottom
        if(topBottom) {
            // length of the line from the leftmost point of two regions to the rightmost point of two regions
            int l = Math.max(startX+width, region.startX + region.width) - Math.min(startX, region.startX);

            // vertical distance between startX points of regions
            int leftBox = Math.abs(startX-region.startX);

            // vertical distance between end points of regions
            int rightBox = Math.abs(startX+width - region.startX-region.width);

            adjacentPoints = l - leftBox - rightBox;
        } else if (leftRight) {
            // length of the line from the topmost point of two regions to the bottommost point of two regions
            int l = Math.max(startY+height, region.startY+region.height) - Math.min(startY, region.startY);

            // horizontal distance between startY points of regions
            int topBox = Math.abs(startY - region.startY);

            // horizontal distance between end points of regions
            int bottomBox = Math.abs(startY+height - region.startY - region.height);

            adjacentPoints = l - topBox - bottomBox;
        }

        return adjacentPoints;
    }

    /**
     * Returns regionSize of this region.
     *
     * @return
     */
    public int getSize() {
       return width*height;
    }

    /**
     * Returns true if difference between min and max value in this region is leesser or equal to threashold.
     * @return True or false if the region is homogenic.
     */
    public boolean isHomogenic() {
        return (maxVal - minVal) <= threshold;
    }

    /**
     * Splits the region to 4, 2 or 1 smaller regions.
     * Regions will have those sizes:
     *
     * @return Regions.
     */
    public Region[] split() {
        if(width == 1 && height == 1) {
            return new Region[] {this};
        }

        if(width == 1) {
            int h = height-1;
            return new Region[] {
                    new Region(startX, startY,1,1, image, threshold),
                    new Region(startX,startY+1,1,h,image, threshold)
            };
        }

        if(height == 1) {
            int w = width-1;
            return new Region[] {
                    new Region(startX, startY,1,1, image, threshold),
                    new Region(startX+1,startY,w,1,image, threshold)
            };
        }

        // overcome problems with rounding
        int w1 = width / 2;
        int h1 = height / 2;
        int w2 = width - w1;
        int h2 = height - h1;

        return new Region[] {
                // top left region
                new Region(startX, startY, w1, h1, image, threshold),

                // top right region
                new Region(startX+w1, startY, w2, h1, image, threshold),

                // bottom left region
                new Region(startX, startY+h1, w1, h2, image, threshold),

                // bottom right region
                new Region(startX+w1, startY+h1, w2, h2, image, threshold)
        };
    }

    /**
     * Returns true if the region is a neighbour of this region.
     * Two regions are neighbours if one these conditions is ture:
     *     x1 = x2 && (y1 - y2 = h2 || y2 - y1 = h1)
     *     y1 = y2 && (x1 - x2 = w2 || x2 - x1 = h1)
     *
     * Image data are ignored by this method.
     *
     * @param region Other region.
     * @return True or false.
     */
    public boolean isNeighbour(Region region) {
        if(neighbours == Constants.NEIGHBOURS_4) {
            return isNeighbour4(region);
        } else {
            return false;
        }
    }

    /**
     * Check if the region is among 4 direct neighbours of this region.
     * @param region Other region.
     * @return True or false.
     */
    private boolean isNeighbour4(Region region) {
        return calculateAdjacentPoints(region) > 0;
    }

    /**
     * Returns true if this region can be merged with the other one. That can be done only if
     * the merged region would be still homogenic.
     * @param r Other region.
     * @return True or false.
     */
    public boolean canMerge(Region r) {
        int newMin = minVal < r.getMinVal() ? minVal : r.getMinVal();
        int newMax = maxVal > r.getMaxVal() ? maxVal : r.getMaxVal();

        return Math.abs(newMax - newMin) <= threshold;
    }

    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region = (Region) o;

        if (startX != region.startX) return false;
        if (startY != region.startY) return false;
        if (width != region.width) return false;
        return height == region.height;
    }

    @Override
    public int hashCode() {
        int result = startX;
        result = 31 * result + startY;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }

}
