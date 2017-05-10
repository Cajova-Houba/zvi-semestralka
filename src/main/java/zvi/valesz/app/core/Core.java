package zvi.valesz.app.core;

import zvi.valesz.app.core.region.MergedRegion;
import zvi.valesz.app.core.region.Region;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Core class for are core-reated operations.
 *
 * Created by Zdenek Vales on 6.5.2017.
 */
public class Core {

    /**
     * Performs a merge operation over a collection of homogene regions.
     * @param regions
     * @return Merged regions. Each list is one independent region in image.
     */
    public static List<MergedRegion> merge(List<Region> regions) {
        List<MergedRegion> mergedRegions = new ArrayList<>();
        int regCntr = 1;

        // merging
        while(!regions.isEmpty()) {
            Region r = regions.remove(0);
            MergedRegion mergedRegion = new MergedRegion(regCntr);
            regCntr++;
            List<Region> buffer = new LinkedList<>();
            buffer.add(r);


            // for every unmarked region of the list, go through it's neighbours
            // and check if they can be merged together
            while(!buffer.isEmpty()) {
                // take region from buffer
                Region bufferR = buffer.remove(0);
                Iterator<Region> regIt = regions.iterator();

                // go through remaining regions and add neighbours that can be merged to the buffer
                while(regIt.hasNext()) {
                    Region tmp = regIt.next();
                    if(bufferR.isNeighbour(tmp) && bufferR.canMerge(tmp)) {
                        buffer.add(tmp);
                        regIt.remove();
                    }
                }

                // add item from buffer to the result region list
                mergedRegion.add(bufferR);
            }

            // add merged region to result list
            mergedRegions.add(mergedRegion);
        }

        return mergedRegions;
    }

    /**
     * Performs a split operation over given Region object.
     * The region is splitted until only homogenic regions remain and those are returned.
     *
     * @param region
     * @return
     */
    public static List<Region> split(Region region) {
        List<Region> regionBuffer = new LinkedList<Region>();
        List<Region> regions = new ArrayList<>();

        // splitting
        regionBuffer.add(region);
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

        return regions;
    }
}
