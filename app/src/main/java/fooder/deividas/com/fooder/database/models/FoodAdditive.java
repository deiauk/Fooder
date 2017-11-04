package fooder.deividas.com.fooder.database.models;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Deividas on 2017-10-25.
 */

@Parcel
public class FoodAdditive extends RealmObject {
    @PrimaryKey
    int id;
    String number;
    String name;
    Description description;
    Category category;
    Danger danger;

    public FoodAdditive() {
    }

    public FoodAdditive(int id, String number, String name, Description description, Category category, Danger danger) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.description = description;
        this.category = category;
        this.danger = danger;
    }

    public FoodAdditive(FoodAdditive foodAdditive, String name) {
        this.id = foodAdditive.getId();
        this.number = foodAdditive.getNumber();
        this.name = name;
        this.description = foodAdditive.getDescription();
        this.category = foodAdditive.getCategory();
        this.danger = foodAdditive.getDanger();
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Danger getDanger() {
        return danger;
    }
}
