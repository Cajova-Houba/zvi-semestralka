package zvi.valesz.app.core;

/**
 * Box class to be used when manually thresholding.
 * It holds the threshold and the value which will be used.
 *
 * Created by Zdenek Vales on 28.5.2017.
 */
public class Threshold implements Comparable<Threshold> {

    public final int threshold;

    public final int newValue;

    public Threshold(int threshold, int newValue) {
        this.threshold = threshold;
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return "{" +
                "threshold=" + threshold +
                ", newValue=" + newValue +
                '}';
    }

    @Override
    public int compareTo(Threshold o) {
        if(threshold == o.threshold) {
            return  0;
        }

        if(threshold < o.threshold) {
            return  -1;
        }

        return 1;
    }
}
