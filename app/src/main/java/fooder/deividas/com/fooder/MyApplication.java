package fooder.deividas.com.fooder;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import fooder.deividas.com.fooder.database.models.FoodAdditive;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Deividas on 2017-10-28.
 */

public class MyApplication extends Application {

    private Realm realm;
    private List<FoodAdditive> foodAdditives;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(configuration);

        realm = Realm.getDefaultInstance();
        foodAdditives = new ArrayList<>();
    }

    public Realm getRealm() {
        return realm;
    }

    public List<FoodAdditive> getDataList() {
        if (foodAdditives.isEmpty()) {
            foodAdditives = realm.where(FoodAdditive.class).findAll();
        }
        return foodAdditives;
    }
}
