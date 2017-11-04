package fooder.deividas.com.fooder.database.models;

import org.parceler.Parcel;

import fooder.deividas.com.fooder.Utils;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Deividas on 2017-10-29.
 */

@Parcel
public class Danger extends RealmObject {
    @PrimaryKey
    int id;
    String name;
    int dangerType;

    public Danger() {
    }

    public Danger(int id, String name) {
        this.id = id;
        this.name = name;

        if (name.toLowerCase().contains("nepavojingas")) {
            dangerType = Utils.SAFE;
        } else if (name.toLowerCase().contains("pavojingas")) {
            dangerType = Utils.DANGEROUS;
        } else {
            dangerType = Utils.AVOID;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDangerLevel() {
        return dangerType;
    }
}
