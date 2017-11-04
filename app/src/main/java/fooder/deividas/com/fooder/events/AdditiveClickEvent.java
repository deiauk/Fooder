package fooder.deividas.com.fooder.events;

import fooder.deividas.com.fooder.database.models.FoodAdditive;

/**
 * Created by Deividas on 2017-10-30.
 */


public class AdditiveClickEvent {
    private int id;

    public AdditiveClickEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
