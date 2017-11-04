package fooder.deividas.com.fooder;

import android.graphics.Color;

/**
 * Created by Deividas on 2017-11-04.
 */

public class ColorHandler {
    public static int getColor(int level) {
        switch (level) {
            case Utils.SAFE:
                return Color.GREEN;
            case Utils.DANGEROUS:
                return Color.RED;
            case Utils.AVOID:
                return Color.YELLOW;
        }
        return 0;
    }
}
