package fooder.deividas.com.fooder.levensitein;

import java.io.Serializable;

/**
 * Created by Deividas on 2017-10-31.
 */

public interface StringDistance extends Serializable {

    /**
     * Compute and return a measure of distance.
     * Must be >= 0.
     * @param s1
     * @param s2
     * @return
     */
    int distance(String s1, String s2);
}
