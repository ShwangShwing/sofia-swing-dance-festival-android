package com.sofiaswing.sofiaswingdancefestival.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.BaseColumns;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class SettingsProvider implements ProvidersInterfaces.ISettingsProvider {
    private final Context context;
    private SettingsDbHelper dbHelper;
    private SQLiteDatabase database;

    public SettingsProvider(Context incomingContext) {
        this.context = incomingContext;
        final SettingsProvider monitorObject = this;

        (new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                synchronized (monitorObject) {
                    dbHelper = new SettingsDbHelper(context);
                    database = dbHelper.getWritableDatabase();
                    monitorObject.notifyAll();
                }

                return null;
            }
        }).execute();
    }

    @Override
    public boolean isSubscribedForEvent(String eventId) {
        String[] projection = {
                SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID,
        };


        String selection = SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID + " = ?";
        String[] selectionArgs = { eventId };


        synchronized (this) {
            while (this.database == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return false;
                }
            }

            Cursor cursor = this.database.query(
                    SettingsContract.EventSubscriptions.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            int count = cursor.getCount();
            cursor.close();

            return count > 0;
        }
    }

    @Override
    public void subscribeForEvent(String eventId, String eventName, int startTimestamp, int notifyTimestamp) {
        ContentValues values = new ContentValues();
        values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID, eventId);
        values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NAME, eventName);
        values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_START_TIME, startTimestamp);
        values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NOTIFY_TIME, startTimestamp);


        synchronized (this) {
            while (this.database == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            if (notifyTimestamp == 0) {
                //get default notification time
                //values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NOTIFY_TIME, );
            }

            this.database.insert(SettingsContract.EventSubscriptions.TABLE_NAME, null, values);
        }
    }

    @Override
    public void unsubscribeFromEvent(String eventId) {
        // Define 'where' part of query.
        String selection = SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { eventId };

        synchronized (this) {
            while (this.database == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            // Issue SQL statement.
            this.database.delete(SettingsContract.EventSubscriptions.TABLE_NAME, selection, selectionArgs);
        }
    }


}

class SettingsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Settings.database";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SettingsContract.EventSubscriptions.TABLE_NAME + " (" +
                    SettingsContract.EventSubscriptions._ID + " INTEGER PRIMARY KEY," +
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID + " TEXT," +
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NAME + " TEXT," +
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_START_TIME + " INTEGER, " +
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NOTIFY_TIME + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SettingsContract.EventSubscriptions.TABLE_NAME;

    public SettingsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Discard old data on update...
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

final class SettingsContract {
    private SettingsContract() {
    }

    /* Inner class that defines the table contents */
    public static class EventSubscriptions implements BaseColumns {
        public static final String TABLE_NAME = "eventSubscriptions";
        public static final String COLUMN_NAME_EVENT_ID = "eventId";
        public static final String COLUMN_NAME_EVENT_NAME = "eventName";
        public static final String COLUMN_NAME_EVENT_START_TIME = "eventStartTime";
        public static final String COLUMN_NAME_EVENT_NOTIFY_TIME = "eventNotifyTime";
    }
}
