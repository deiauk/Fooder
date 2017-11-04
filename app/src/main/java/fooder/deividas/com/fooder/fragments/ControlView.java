package fooder.deividas.com.fooder.fragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.otaliastudios.cameraview.CameraView;

import java.util.ArrayList;
import java.util.List;

import fooder.deividas.com.fooder.R;
import fooder.deividas.com.fooder.database.models.FoodAdditive;

/**
 * Created by Deividas on 2017-11-01.
 */

public class ControlView extends LinearLayout {

    public interface Callback {
        boolean onValueChanged(Control control, Object value, String name);
    }

    private FoodAdditive value;
    private ArrayList<FoodAdditive> values;
    private ArrayList<String> valuesStrings;
    private Callback callback;
    private Spinner spinner;

    public ControlView(Context context, FoodAdditive control, Callback callback) {
        super(context);
        this.callback = callback;
        setOrientation(VERTICAL);

        inflate(context, R.layout.food_additive_item, this);
        TextView number = findViewById(R.id.title);
        number.setText(control.getNumber());

        TextView name = findViewById(R.id.name);
        name.setText(control.getName());

        View divider = findViewById(R.id.divider);
       // divider.setVisibility(control.isSectionLast() ? View.VISIBLE : View.GONE);

        ViewGroup content = findViewById(R.id.content);
        spinner = new Spinner(context, Spinner.MODE_DROPDOWN);
        content.addView(spinner);
    }

    @SuppressWarnings("all")
    public void onCameraOpened(CameraView view) {
        /*
        values = new ArrayList(control.getValues(view));
        value = (FoodAdditives) control.getCurrentValue(view);
        valuesStrings = new ArrayList<>();
        for (FoodAdditives value : values) {
            valuesStrings.add(value.);
        }

        if (values.isEmpty()) {
            spinner.setOnItemSelectedListener(null);
            spinner.setEnabled(false);
            spinner.setAlpha(0.8f);
//            spinner.setAdapter(new ArrayAdapter(getContext(),
//                    R.layout.spinner_text, new String[]{ "Not supported." }));
            spinner.setSelection(0, false);
        } else {
            spinner.setEnabled(true);
            spinner.setAlpha(1f);
//            spinner.setAdapter(new ArrayAdapter(getContext(),
//                    R.layout.spinner_text, valuesStrings));
            spinner.setSelection(values.indexOf(value), false);
        }
        */
    }
}
