package zvi.valesz.app.core;

/**
 * Class containing statistic constants to be used in hash map.
 *
 * Created by Zdenek Vales on 28.5.2017.
 */
public class Statistics {

    /**
     * Threshold of the segmentation.
     * Float number should be a value.
     */
    public static final String THRESHOLD = "threshold";

    /**
     * Histogram of the image.
     * Int array should be a value.
     */
    public static final String HISTOGRAM = "histogram";

    /**
     * Total regions found in the image.
     * Int should be a value.
     */
    public static final String TOTAL_REGIONS_COUNT = "totalRegionsCount";

    /**
     * Number of merged regions.
     * Int should be a value.
     */
    public static final String MERGED_REGIONS_COUNT = "mergedRegionsCount";

    /**
     * Number of pixels in inmage.
     * Int should be a value.
     */
    public static final String PIXEL_COUNT = "pixelCount";
}
