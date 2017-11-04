package fooder.deividas.com.fooder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.otaliastudios.cameraview.AspectRatio;
import com.otaliastudios.cameraview.CameraUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import fooder.deividas.com.fooder.MyApplication;
import fooder.deividas.com.fooder.R;
import fooder.deividas.com.fooder.adapters.FoodAdditivesAdapter;
import fooder.deividas.com.fooder.database.models.FoodAdditive;
import fooder.deividas.com.fooder.events.AdditiveClickEvent;
import fooder.deividas.com.fooder.fragments.Control;
import fooder.deividas.com.fooder.fragments.ControlView;
import io.fotoapparat.photo.BitmapPhoto;
import io.realm.Realm;

public class DetailCameraAdditiveActivity extends AppCompatActivity implements ControlView.Callback {
    private ImageView imageView;
    private ViewGroup controlPanel;

    private static Bitmap bitmapPhoto;
    public static final String IDS = "ids";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_camera_additive);

        imageView = findViewById(R.id.image);
        imageView.setImageBitmap(bitmapPhoto);

        ArrayList<Integer> ids = getIntent().getIntegerArrayListExtra(IDS);
        if (ids.isEmpty()) {
            Log.d("ASDIUASDSADSD", "TUSCIAAA");
            return;
        }
        Integer[] list =  ids.toArray(new Integer[ids.size()]);

        Log.d("ASDIUASDSADSD", ids.size() + " " + list.length);

        Realm realm = ((MyApplication) getApplicationContext()).getRealm();
        List<FoodAdditive> foodAdditives = realm.where(FoodAdditive.class).in("id", list).findAll();

        controlPanel = findViewById(R.id.controls);
        RecyclerView recyclerView = (RecyclerView) controlPanel.getChildAt(0);
        FoodAdditivesAdapter additivesAdapter = new FoodAdditivesAdapter(foodAdditives);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(additivesAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation()));

        controlPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
                b.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        imageView.setImageBitmap(bitmapPhoto);
        //imageView.setRotation(-bitmapPhoto.rotationDegrees);

        FloatingActionButton ii = findViewById(R.id.showAdditivesList);
        ii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
    }

    public static void setImage(@Nullable Bitmap im) {
        bitmapPhoto = im;
        //image = im != null ? new WeakReference<>(im) : null;
    }

    @Override
    public boolean onValueChanged(Control control, Object value, String name) {
        return false;
    }

    private void edit() {
        BottomSheetBehavior b = BottomSheetBehavior.from(controlPanel);
        b.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onAdditiveClick(AdditiveClickEvent obj) {
        Intent intent = new Intent(getApplicationContext(), DetailAdditiveActivity.class);
        intent.putExtra(DetailAdditiveActivity.ID, obj.getId());
        startActivity(intent);
    }
}
