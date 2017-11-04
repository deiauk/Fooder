package fooder.deividas.com.fooder.levensitein;

import me.xdrop.fuzzywuzzy.model.ExtractedResult;

/**
 * Created by Deividas on 2017-11-01.
 */

public class CustomExtractedResult extends ExtractedResult {
    private int id;

    public CustomExtractedResult(String string, int score, int id) {
        super(string, score);

        this.id = id;
    }

    public int getId() {
        return id;
    }
}
