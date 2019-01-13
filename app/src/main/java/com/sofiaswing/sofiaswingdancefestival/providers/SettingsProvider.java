package com.sofiaswing.sofiaswingdancefestival.providers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import com.sofiaswing.sofiaswingdancefestival.utils.EventSubscriptionAlarmReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shwangshwing on 10/11/17.
 */

public class SettingsProvider implements ProvidersInterfaces.ISettingsProvider {
    private final String SETTING_ID_NOTIFICATION_ADVANCE_TIME_SECONDS = "notification_advance_time_seconds";
    private final String SETTING_ID_ARE_NEWS_NOTIFICATIONS_DISABLED = "news_notifications_enabled";

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
        String[] selectionArgs = {eventId};


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
    public void subscribeForEvent(String eventId, String eventName, long startTimestamp, long notifyTimestamp) {
        ContentValues values = new ContentValues();
        values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID, eventId);
        values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NAME, eventName);
        values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_START_TIME, startTimestamp);
        values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NOTIFY_TIME,
                startTimestamp - DefaultSettingValues.EVENT_NOTIFICATION_TIME_S);

        synchronized (this) {
            while (this.database == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            if (notifyTimestamp == 0) {
                notifyTimestamp = startTimestamp - getEventsNotificationAdvanceTimeSeconds();
            }

            values.put(SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NOTIFY_TIME, notifyTimestamp);

            this.database.insert(SettingsContract.EventSubscriptions.TABLE_NAME, null, values);
        }

        setNotificationAlarmIfNotTooLate(eventId, eventName, startTimestamp, notifyTimestamp);
    }

    @Override
    public synchronized void updateEventSubscription(String eventId, String eventName, long startTimestamp, long notifyTimestamp) {
        if (this.isSubscribedForEvent(eventId)) {
            while (this.database == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            String[] projection = {
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_START_TIME,
            };

            String[] selArgs = { eventId };
            Cursor cursor = this.database.query(
                    SettingsContract.EventSubscriptions.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    String.format("%s = ?", SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID),                                // The columns for the WHERE clause
                    selArgs ,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            int dbStartTimestamp = 0;
            if (cursor.moveToNext()) {
                dbStartTimestamp = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_START_TIME));
            }

            cursor.close();


            if (dbStartTimestamp != startTimestamp) {
                this.unsubscribeFromEvent(eventId);
                this.subscribeForEvent(eventId, eventName, startTimestamp, notifyTimestamp);
            }
        }
    }

    @Override
    public void unsubscribeFromEvent(String eventId) {
        // Define 'where' part of query.
        String selection = SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {eventId};

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

        this.cancelNotificationAlarm(eventId);
    }

    @Override
    public long getEventsNotificationAdvanceTimeSeconds() {
        long result = DefaultSettingValues.EVENT_NOTIFICATION_TIME_S;

        try {
            result = Integer.parseInt(getSettingValueById(SETTING_ID_NOTIFICATION_ADVANCE_TIME_SECONDS));
        } catch (Exception e) {

        }

        return result;
    }

    @Override
    public void setEventsNotificationAdvanceTimeSeconds(long seconds) {
        this.setSettingValue(SETTING_ID_NOTIFICATION_ADVANCE_TIME_SECONDS, String.valueOf(seconds));
    }

    @Override
    public synchronized void setupAllNotificationAlarms() {
            while (this.database == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            String[] projection = {
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID,
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NAME,
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_START_TIME,
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NOTIFY_TIME,
            };

            Cursor cursor = this.database.query(
                    SettingsContract.EventSubscriptions.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            while (cursor.moveToNext()) {
                String eventId = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID));
                String eventName = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NAME));
                int startTimestamp = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_START_TIME));
                int notifyTimestamp = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NOTIFY_TIME));

                this.setNotificationAlarmIfNotTooLate(eventId, eventName, startTimestamp, notifyTimestamp);
            }

            cursor.close();
    }

    @Override
    public synchronized List<String> getSubscribedEventsIds() {
        List<String> result = new ArrayList<>();

        while (this.database == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                return result;
            }
        }

        String[] projection = {
                SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID,
        };

        Cursor cursor = this.database.query(
                SettingsContract.EventSubscriptions.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        while (cursor.moveToNext()) {
            String eventId = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID));

            result.add(eventId);
        }

        cursor.close();

        return result;
    }

    @Override
    public boolean areNewsNotificationsEnabled() {
        // reverse conditions set the initial state of the setting to be "enabled"
        return !this.getSettingValueById(SETTING_ID_ARE_NEWS_NOTIFICATIONS_DISABLED).equals("true");
    }

    @Override
    public void enableNewsNotifications() {
        this.setSettingValue(SETTING_ID_ARE_NEWS_NOTIFICATIONS_DISABLED, "false");
    }

    @Override
    public void disableNewsNotifications() {
        this.setSettingValue(SETTING_ID_ARE_NEWS_NOTIFICATIONS_DISABLED, "true");
    }

    private String getSettingValueById(String settingId) {
        synchronized (this) {
            while (this.database == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return "";
                }
            }

            String[] projection = {
                    SettingsContract.Settings.COLUMN_NAME_SETTING_VALUE
            };

            String selection = SettingsContract.Settings.COLUMN_NAME_SETTING_ID + " = ?";
            String[] selectionArgs = {settingId};

            Cursor cursor = this.database.query(
                    SettingsContract.Settings.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            String result = "";
            if (cursor.moveToNext()) {
                result = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                SettingsContract.Settings.COLUMN_NAME_SETTING_VALUE));
            }

            cursor.close();

            return result;
        }
    }

    private void setSettingValue(String settingId, String settingValue) {
        String[] projection = {
                SettingsContract.Settings.COLUMN_NAME_SETTING_ID,
        };


        String selection = SettingsContract.Settings.COLUMN_NAME_SETTING_ID + " = ?";
        String[] selectionArgs = {settingId};

        ContentValues settingDbRowValues = new ContentValues();
        settingDbRowValues.put(SettingsContract.Settings.COLUMN_NAME_SETTING_VALUE, settingValue);

        synchronized (this) {
            while (this.database == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }

            Cursor cursor = this.database.query(
                    SettingsContract.Settings.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            boolean settingExists = cursor.getCount() > 0;
            cursor.close();

            if (settingExists) {
                this.database.update(
                        SettingsContract.Settings.TABLE_NAME,
                        settingDbRowValues,
                        selection,
                        selectionArgs
                );
            } else {
                settingDbRowValues.put(SettingsContract.Settings.COLUMN_NAME_SETTING_ID, settingId);
                this.database.insert(SettingsContract.Settings.TABLE_NAME, null, settingDbRowValues);
            }
        }
    }

    private void setNotificationAlarmIfNotTooLate(
            String eventId,
            String eventName,
            long startTimestamp,
            long notifyTimestamp) {

        this.cancelNotificationAlarm(eventId);
        if (System.currentTimeMillis() / 1000 > startTimestamp) {
            // The event has already started. Don't notify.
            return;
        }

        Intent eventNotificationIntent = new Intent(this.context, EventSubscriptionAlarmReceiver.class);
        eventNotificationIntent.putExtra(EventSubscriptionAlarmReceiver.EVENT_ID_KEY, eventId);
        eventNotificationIntent.putExtra(EventSubscriptionAlarmReceiver.EVENT_NAME_KEY, eventName);
        eventNotificationIntent.putExtra(EventSubscriptionAlarmReceiver.EVENT_START_TIME_KEY, startTimestamp);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        this.context,
                        eventId.hashCode(),
                        eventNotificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        long executionTimestampMs = (long) notifyTimestamp * 1000;
        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, executionTimestampMs, pendingIntent);
    }

    private void cancelNotificationAlarm(String eventId) {
        Intent eventNotificationIntent = new Intent(this.context, EventSubscriptionAlarmReceiver.class);
        eventNotificationIntent.putExtra(EventSubscriptionAlarmReceiver.EVENT_ID_KEY, eventId);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        this.context,
                        eventId.hashCode(),
                        eventNotificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}

class SettingsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Settings.database";

    private static final String SQL_CREATE_EVENT_SUBSCRIPTIONS =
            "CREATE TABLE " + SettingsContract.EventSubscriptions.TABLE_NAME + " (" +
                    SettingsContract.EventSubscriptions._ID + " INTEGER PRIMARY KEY," +
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_ID + " TEXT," +
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NAME + " TEXT," +
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_START_TIME + " INTEGER, " +
                    SettingsContract.EventSubscriptions.COLUMN_NAME_EVENT_NOTIFY_TIME + " INTEGER);";
    private static final String SQL_CREATE_SETTINGS =
            "CREATE TABLE " + SettingsContract.Settings.TABLE_NAME + " (" +
                    SettingsContract.Settings._ID + " INTEGER PRIMARY KEY," +
                    SettingsContract.Settings.COLUMN_NAME_SETTING_ID + " TEXT," +
                    SettingsContract.Settings.COLUMN_NAME_SETTING_VALUE + " TEXT)";

    private static final String SQL_DELETE_SETTINGS =
            "DROP TABLE IF EXISTS " + SettingsContract.EventSubscriptions.TABLE_NAME + ";";
    private static final String SQL_DELETE_SUBSCRIPTIONS =
            "DROP TABLE IF EXISTS " + SettingsContract.Settings.TABLE_NAME;

    public SettingsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENT_SUBSCRIPTIONS);
        db.execSQL(SQL_CREATE_SETTINGS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Discard old data on update...
        db.execSQL(SQL_DELETE_SETTINGS);
        db.execSQL(SQL_DELETE_SUBSCRIPTIONS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

final class SettingsContract {
    private SettingsContract() {
    }

    public static class EventSubscriptions implements BaseColumns {
        public static final String TABLE_NAME = "eventSubscriptions";
        public static final String COLUMN_NAME_EVENT_ID = "eventId";
        public static final String COLUMN_NAME_EVENT_NAME = "eventName";
        public static final String COLUMN_NAME_EVENT_START_TIME = "eventStartTime";
        public static final String COLUMN_NAME_EVENT_NOTIFY_TIME = "eventNotifyTime";
    }

    public static class Settings implements BaseColumns {
        public static final String TABLE_NAME = "Settings";
        public static final String COLUMN_NAME_SETTING_ID = "settingId";
        public static final String COLUMN_NAME_SETTING_VALUE = "settingValue";
    }
}
