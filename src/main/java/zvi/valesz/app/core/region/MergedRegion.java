package zvi.valesz.app.core.region;

import java.util.ArrayList;

/**
 * A wrapper for a list of homogenic regions merged together.
 *
 * Created by valesz on 10.05.2017.
 */
public class MergedRegion extends ArrayList<Region>{

    public final int regionId;

    public MergedRegion(int regionId) {
        this.regionId = regionId;
    }
}
