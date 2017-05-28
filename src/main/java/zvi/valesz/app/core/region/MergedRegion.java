package zvi.valesz.app.core.region;

import java.util.ArrayList;

/**
 * A wrapper for a list of homogenic regions merged together.
 *
 * Created by valesz on 10.05.2017.
 */
public class MergedRegion extends ArrayList<Region>{

    public final int regionId;

    /**
     * Total size of this region.
     * Changes with add and remove methods.
     */
    private int regionSize;

    public MergedRegion(int regionId) {
        this.regionSize = 0;
        this.regionId = regionId;
    }

    /**
     * Returns regionSize of this region.
     * @return
     */
    public int getRegionSize() {
        return regionSize;
    }

    @Override
    public boolean add(Region region) {
        regionSize += region.getSize();
        return super.add(region);
    }

    @Override
    public void add(int index, Region element) {
        regionSize += element.getSize();
        super.add(index, element);
    }

    @Override
    public Region remove(int index) {
        Region r =super.remove(index);
        regionSize -= r.getSize();
        return r;
    }

    @Override
    public boolean remove(Object o) {
        if(contains(o)) {
            regionSize -= ((Region)o).getSize();
        }
        return super.remove(o);
    }

    /**
     * Average color of the merged region got as (min+max)/2.
     * If the merged region is empty, 0 is returned.
     *
     * @return Average color of the region.
     */
    public int getAverageColor() {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for(Region region : this) {
            int tmpMin = region.getMinVal();
            int tmpMax = region.getMaxVal();

            if(tmpMin < min) {
                min = tmpMin;
            }

            if(tmpMax > max) {
                max = tmpMax;
            }
        }

        return (min+max) / 2;
    }
}
