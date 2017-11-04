package fooder.deividas.com.fooder.levensitein;

import java.util.Collection;
import java.util.List;

import fooder.deividas.com.fooder.database.models.FoodAdditive;
import me.xdrop.fuzzywuzzy.Extractor;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.algorithms.WeightedRatio;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

/**
 * Created by Deividas on 2017-11-01.
 */

public class CustomFuzzySearch extends FuzzySearch {
    public static List<CustomExtractedResult> extractSorted(String query, List<FoodAdditive> choices,
                                                      int cutoff) {

        CustomExtractor extractor = new CustomExtractor(cutoff);

        return extractor.extractTop(query, choices, new WeightedRatio());

    }
}
