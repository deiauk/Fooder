package fooder.deividas.com.fooder.levensitein;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fooder.deividas.com.fooder.database.models.FoodAdditive;
import me.xdrop.fuzzywuzzy.Applicable;
import me.xdrop.fuzzywuzzy.Extractor;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

/**
 * Created by Deividas on 2017-11-01.
 */

public class CustomExtractor extends Extractor {

    public CustomExtractor(int cutoff) {
        setCutoff(cutoff);
    }

    public List<CustomExtractedResult> extractTop(String query, List<FoodAdditive> choices, Applicable func) {

        List<CustomExtractedResult> best = extractWithoutOrder(query, choices, func);
        Collections.sort(best, Collections.<ExtractedResult>reverseOrder());

        return best;
    }

    private List<CustomExtractedResult> extractWithoutOrder(String query, List<FoodAdditive> foodAdditives, Applicable func) {
        List<CustomExtractedResult> yields = new ArrayList<>();

        for (FoodAdditive choices : foodAdditives) {
            String s = choices.getName();
            int score = func.apply(query, s);
            if (score >= getCutoff()) {
                yields.add(new CustomExtractedResult(s, score, choices.getId()));
            }
        }
        return yields;

    }

}
