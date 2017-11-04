package fooder.deividas.com.fooder.database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fooder.deividas.com.fooder.MyApplication;
import fooder.deividas.com.fooder.database.models.Category;
import fooder.deividas.com.fooder.database.models.Danger;
import fooder.deividas.com.fooder.database.models.Description;
import fooder.deividas.com.fooder.database.models.FoodAdditive;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Deividas on 2017-10-25.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "additives.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private File file;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;

        File root = new File(Environment.getExternalStorageDirectory(), "database");
        if (!root.exists()) {
            root.mkdirs();
        }
        file = new File(root, DB_NAME);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database " + e.getMessage());
            }
        }
        migrateToRealm();
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {

        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open("database/" + DB_NAME);
        OutputStream myOutput = new FileOutputStream(file);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    private List<RealmObject> getList(int type, String tableName) {
        String query = "SELECT Id, Name FROM " + tableName + " ORDER BY Id ASC";
        List<RealmObject> listToAdd = new ArrayList<>();
        Cursor cursor = myDataBase.rawQuery(query, null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                RealmObject obj = null;
                if (type == 1) {
                    obj = new Category(id, name);
                } else if (type == 2) {
                    obj = new Danger(id, name);
                } else if (type == 3) {
                    obj = new Description(id, name);
                }
                listToAdd.add(obj);
            }
        } finally {
            cursor.close();
        }

        return listToAdd;
    }

    private List<RealmObject> getFoodAdditives(List<Category> categories, List<Danger> dangers, List<Description> descriptions) {
        String query = "SELECT Id, Number, Name, CategoryId, DangerId, DescriptionId FROM FoodAdditives";

        Cursor cursor = myDataBase.rawQuery(query, null);
        List<RealmObject> list = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String number = cursor.getString(1);
                String name = cursor.getString(2);
                int category = cursor.getInt(3);
                int danger = cursor.getInt(4);
                int description = cursor.getInt(5);

                Category categoryVal = categories.get(category - 1);
                Danger dangerVal = dangers.get(danger - 1);
                Description descriptionVal = descriptions.get(description - 1);

                FoodAdditive foodAdditive = new FoodAdditive(id, number, name, descriptionVal, categoryVal, dangerVal);
                list.add(foodAdditive);

                Log.d("ADUIASDSD", id + " " + number + " " + name + " " + description + " " + danger + " " + category);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    private void migrateToRealm() {
        final Realm realm = ((MyApplication) myContext).getRealm();
        if (realm.isEmpty()) {
            myDataBase = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READONLY);

            new AsyncTask<Void, Void, List<FoodAdditive>>() {
                @Override
                protected List<FoodAdditive> doInBackground(Void... params) {
                    List<Category> categories = (ArrayList<Category>)(ArrayList<?>)getList(1, "Categories");
                    List<Danger> dangerousList = (ArrayList<Danger>)(ArrayList<?>)getList(2, "DangerList");
                    List<Description> descriptionList = (ArrayList<Description>)(ArrayList<?>)getList(3, "Descriptions");
                    List<FoodAdditive> foodAdditives = (ArrayList<FoodAdditive>)(ArrayList<?>) getFoodAdditives(categories, dangerousList, descriptionList);

                    return foodAdditives;
                }

                @Override
                protected void onPostExecute(List<FoodAdditive> list) {
                    super.onPostExecute(list);

                    myDataBase.close();

                    realm.beginTransaction();
                    realm.copyToRealm(list);
                    realm.commitTransaction();
                }
            }.execute();
        }
    }
}
