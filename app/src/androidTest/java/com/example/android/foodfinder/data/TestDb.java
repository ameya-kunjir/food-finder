package com.example.android.foodfinder.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by ameya on 5/13/2017.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(FoodFinderDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();

        tableNameHashSet.add(FoodFinderContract.RestaurantEntry.TABLE_NAME);
        // tableNameHashSet.add(FoodFinderContract.PhotoEntry.TABLE_NAME);
        tableNameHashSet.add(FoodFinderContract.DetailsEntry.TABLE_NAME);
        tableNameHashSet.add(FoodFinderContract.ReviewEntry.TABLE_NAME);

        deleteTheDatabase();

        SQLiteDatabase db = new FoodFinderDbHelper(this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        Iterator<String> it = tableNameHashSet.iterator();

        while (it.hasNext()) {
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            assertNotNull("Table does not exist: ", c);
            it.next();
            c.close();
        }

    }
}
