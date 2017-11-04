package fooder.deividas.com.fooder.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcel;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import fooder.deividas.com.fooder.ColorHandler;
import fooder.deividas.com.fooder.MyApplication;
import fooder.deividas.com.fooder.R;
import fooder.deividas.com.fooder.database.models.FoodAdditive;
import io.realm.Realm;

public class DetailAdditiveActivity extends AppCompatActivity {
    public static final String ID = "id";

    @BindView(R.id.number)
    TextView number;

    @BindView(R.id.fullName)
    TextView fullName;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.danger)
    TextView danger;

    @BindView(R.id.description)
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_additive);
        ButterKnife.bind(this);

        int id = getIntent().getIntExtra(ID, -1);

        Realm realm = ((MyApplication) getApplication()).getRealm();

        if (id != -1) {
            FoodAdditive foodAdditive = realm.where(FoodAdditive.class).equalTo("id", id).findFirst();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(foodAdditive.getNumber());
            setSupportActionBar(toolbar);

            number.setTextColor(ColorHandler.getColor(foodAdditive.getDanger().getDangerLevel()));
            number.setText(foodAdditive.getNumber());
            fullName.setText(foodAdditive.getName());
            category.setText(foodAdditive.getCategory().getName());
            danger.setText(foodAdditive.getDanger().getName());
            description.setText(foodAdditive.getDescription().getName());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
