package fooder.deividas.com.fooder.database.models;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Deividas on 2017-10-29.
 */

@Parcel
public class Description extends RealmObject {
    @PrimaryKey
    int id;
    String name;

    public Description() {
    }

    public Description(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
