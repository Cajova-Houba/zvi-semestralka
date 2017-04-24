package zvi.valesz.app.core;

/**
 * A region in image.
 *
 * Created by Zdenek Vales on 24.4.2017.
 */
// todo: test
public class Region {

    private final int startX;
    private final int startY;
    private final int width;
    private final int height;
    private final int[][] image;

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
        if(startX < 0 || startY < 0) {
            throw new IllegalArgumentException("Wrong start position!");
        }

        if(width <=0 || height <= 0) {
            throw new IllegalArgumentException("Wrong dimensions!");
        }

        if(image.length <= 0 || image.length < (startX+width) || image[0].length <= 0 || image[0].length < (startY+height)) {
            throw new IllegalArgumentException("Wrong image!");
        }

        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * Returns true if every data pixel in this region has same value.
     * @return True or false if the region is homogenic.
     */
    public boolean isHomogenic() {
        int first = image[startX][startY];
        for(int i = startX; i < startX + width; i++) {
            for (int j = startY; j < startY+height; j++) {
                if(first != image[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Splits the region to 4, 2 or 1 smaller regions.
     * Regions will have those sizes:
     *
     * @return Regions.
     */
    public Region[] split() {
        if(width == 1 && height == 2) {
            return new Region[] {
                    new Region(startX, startY,1,1, image),
                    new Region(startX,startY+1,1,1,image)
            };
        }

        if(width == 2 && height == 1) {
            return new Region[] {
                    new Region(startX, startY,1,1, image),
                    new Region(startX+1,startY,1,1,image)
            };
        }

        if(width == 1 && height == 1) {
            return new Region[] {this};
        }

        // overcome problems with rounding
        int w1 = width / 2;
        int h1 = height / 2;
        int w2 = width - w1;
        int h2 = height - h1;

        return new Region[] {
                // top left region
                new Region(startX, startY, w1, h1, image),

                // top right region
                new Region(startX+w1, startY, w2, h1, image),

                // bottom left region
                new Region(startX, startY+h1, w1, h2, image),

                // bottom right region
                new Region(startX+w1, startY+h1, w2, h2, image)
        };
    }
}
