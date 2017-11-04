package fooder.deividas.com.fooder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;

import java.io.IOException;

import fooder.deividas.com.fooder.database.DatabaseHelper;
import fooder.deividas.com.fooder.fragments.CameraFragment;
import fooder.deividas.com.fooder.fragments.FoodAdditivesListFragment;

public class MainActivity extends AppCompatActivity implements AAH_FabulousFragment.Callbacks {

    private static final int PERMISSION = 4;
    private FragmentManager fragmentManager;
    private int activeId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (activeId != item.getItemId()) {
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container, new FoodAdditivesListFragment())
                                .commit();
                        activeId = item.getItemId();
                        return true;
                    }
                    return false;
                case R.id.navigation_dashboard:
                    if (activeId != item.getItemId()) {
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container, new CameraFragment())
                                .commit();
                        activeId = item.getItemId();
                        return true;
                    }
                    return false;
                case R.id.navigation_notifications:
                    if (activeId != item.getItemId()) {
                        activeId = item.getItemId();
                        return true;
                    }
                    return false;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatabase();

        fragmentManager = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (activeId != R.id.navigation_home) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new FoodAdditivesListFragment())
                    .commit();
            activeId = R.id.navigation_home;
        }
    }

    private void initDatabase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                createDatabase();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                        PERMISSION);
            }
        } else {
            createDatabase();
        }
    }

    private void createDatabase() {
        DatabaseHelper myDbHelper = new DatabaseHelper(getApplicationContext());
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION:
                boolean isAllPermissionGranted = true;
                if (grantResults.length > 0 && permissions.length == grantResults.length) {
                    for (int i = 0; i < permissions.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            isAllPermissionGranted = false;
                            break;
                        }
                    }
                }
                if (isAllPermissionGranted){
                    createDatabase();
                }
                break;
        }
    }

    @Override
    public void onResult(Object result) {

    }
}