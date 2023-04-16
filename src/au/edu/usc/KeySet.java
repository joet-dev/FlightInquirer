package au.edu.usc;

/**
 * Key set is a class used to store a flight load factors index and value.
 *
 * @Author Joseph Thurlow
 */
public class KeySet {
    private final int idx;
    private final Double val;

    public KeySet(int index, Double value) {
        idx = index;
        val = value;
    }

    public int getIdx() {
        return idx;
    }

    public Double getVal() {
        return val;
    }

    public String toString() {
        return "{" + idx + ", " + val + "}";
    }
}
