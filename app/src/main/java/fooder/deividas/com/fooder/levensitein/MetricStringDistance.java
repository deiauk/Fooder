package fooder.deividas.com.fooder.levensitein;

/**
 * Created by Deividas on 2017-10-31.
 */

public interface MetricStringDistance extends StringDistance {

    /**
     * Compute and return the metric distance.
     * @param s1
     * @param s2
     * @return
     */
    int distance(String s1, String s2);
}
