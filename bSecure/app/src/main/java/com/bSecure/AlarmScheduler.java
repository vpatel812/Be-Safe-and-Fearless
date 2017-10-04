package com.bSecure;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmScheduler {

    static AlarmManager alarmManager;
    static PendingIntent alarmIntent;
    static SharedPreferences appSetting;
    static String sig_msg;
    static Integer greenTime, yellowtime, redtime;
    static String gmsg;
    static String ymsg;
    static String rmsg;
    static Database db;
    static SharedPreferences.Editor sharedPrefEditor;
    static int xc = 0, start = 0;
    static Context con;

    public AlarmScheduler() {
        // TODO Auto-generated constructor stub
    }

    static void scheduleAlarms(Context ctxt, String msg) {
        SmsManager sms = SmsManager.getDefault();
        con = ctxt;
        appSetting = ctxt.getSharedPreferences("signal", 0);
        sharedPrefEditor = appSetting.edit();
        String sm;
        db = new Database(ctxt);
        Log.v("contxt", String.valueOf(ctxt));
        alarmManager = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        // GREEN
        if (msg.equals("green")) {

            set_msg("green");
            sm = get_msg();
            Log.v("hi", "gcreate" + sm);
            gmsg = appSetting.getString("gmsg1", "");
            String g = gmsg;
            String s = appSetting.getString("gmsg", "") + " " + g;
            Log.v("gmsg", s);
            Intent intent = new Intent(ctxt, AudioRecorder.class);

            // add infos for the service which file to download and where to store
            intent.putExtra("audio", "loc");
            ctxt.startService(intent);
            Cursor c1 = db.getContact("green");
            if (c1.getCount() > 0) {
                if (c1.moveToLast()) {
                    for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
                        Log.v("con", c1.getString(1));
                        try {
                            Log.v("msg", c1.getString(1));
                        } catch (Exception e) {
                            Log.v("msg", "send fail!");
                        }
                    }
                }
            } else {
                Log.v("data", "not found");
                Intent ii = new Intent(ctxt, ApplicationSettings.class);
                ii.putExtra("signal", "green");
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctxt.startActivity(ii);
                xc = 1;

            }
            db.close();

            Intent i = new Intent(ctxt, MessageSender.class);
            Log.v("hi", "gcreate");
            alarmIntent = PendingIntent.getService(ctxt, 0, i, 0);

            greenTime = appSetting.getInt("gtime", 0);
            Log.v("greenTime", greenTime.toString());
            if (xc == 1) {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, 0, alarmIntent);

                xc = 0;
            } else {
                if (greenTime == 0) {

                    Log.v("set", "time");

                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                            SystemClock.elapsedRealtime() + 5000, 5000,
                            alarmIntent);
                } else {
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                            SystemClock.elapsedRealtime() + 5000, greenTime * 1000,
                            alarmIntent);
                }
            }
        }

        // YELLOW
        if (msg.equals("yellow")) {
            set_msg("yellow");
            sm = get_msg();
            Log.v("hi", "ycreate" + sm);
            gmsg = appSetting.getString("ymsg1", "");
            String y = gmsg;
            String s = appSetting.getString("ymsg", "") + " " + y;
            Log.v("ymsg", s);
            Intent intent = new Intent(ctxt, AudioRecorder.class);
            // add infos for the service which file to download and where to store
            intent.putExtra("audio", "loc");
            ctxt.startService(intent);
            Cursor c1 = db.getContact("yellow");
            if (c1.getCount() > 0) {
                if (c1.moveToLast()) {
                    for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
                        Log.v("con", c1.getString(1));
                        try {
                            Log.v("msg", c1.getString(1));
                        } catch (Exception e) {
                            Log.v("msg", "send fail!");
                        }
                    }
                }
            } else {
                Log.v("data", "not found");
                Intent ii = new Intent(ctxt, ApplicationSettings.class);
                ii.putExtra("signal", "yellow");
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctxt.startActivity(ii);
                xc = 1;

            }

            db.close();

            Intent i = new Intent(ctxt, MessageSender.class);
            Log.v("hi", "ycreate");
            alarmIntent = PendingIntent.getService(ctxt, 0, i, 0);

            greenTime = appSetting.getInt("ytime", 0);
            Log.v("yellowTime", greenTime.toString());
            if (xc == 1) {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, 0, alarmIntent);

                xc = 0;
            } else {
                if (greenTime == 0) {

                    Log.v("set", "time");

                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                            SystemClock.elapsedRealtime() + 5000, 5000,
                            alarmIntent);
                } else {
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                            SystemClock.elapsedRealtime() + 5000, greenTime * 1000,
                            alarmIntent);
                }
            }
        }

        // RED
        if (msg.equals("red")) {
            set_msg("red");
            sm = get_msg();

            Intent intent1 = new Intent(ctxt, AudioRecorder.class);
            // add infos for the service which file to download and where to
            // store
            intent1.putExtra("audio", "loc");
            ctxt.startService(intent1);

            ctxt.stopService(new Intent(ctxt, AudioRecorder.class));

            Intent intent = new Intent(ctxt, AudioRecorder.class);
            intent.putExtra("audio", "start");
            start = 1;
            ctxt.startService(intent);

            Intent i = new Intent(ctxt, MessageSender.class);

            Log.v("hi", "rcreate " + msg + sm);
            rmsg = appSetting.getString("rmsg1", "");
            String g = rmsg;
            String s = appSetting.getString("rmsg", "") + " " + g;
            Log.v("red", s);

            Cursor c1 = db.getContact("red");
            if (c1.getCount() > 0) {
                if (c1.moveToLast()) {
                    for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
                        Log.v("con", c1.getString(1));
                        try {

                            Log.v("msg", c1.getString(1));
                        } catch (Exception e) {
                            Log.v("msg", "send fail!");
                        }
                    }

                }
            } else {
                Toast.makeText(con, "Add contact in Application Settings page",
                        Toast.LENGTH_SHORT).show();
                Intent ii = new Intent(con, ApplicationSettings.class);
                ii.putExtra("signal", "red");
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                con.startActivity(ii);
                xc = 1;
                Log.v("data", "not found");

            }
            db.close();
            // r.startRec();
            Log.v("com", "after set no");
            alarmIntent = PendingIntent.getService(ctxt, 0, i, 0);
            if (xc == 1) {
                Log.v("red", "come");
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, 0, alarmIntent);

                xc = 0;
            } else {
                redtime = appSetting.getInt("rtime", 0);
                if (redtime == 0) {

                    Log.v("set", "time");
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                            SystemClock.elapsedRealtime() + 5000, 900 * 1000,
                            alarmIntent);
                } else {
                    Log.v("set", "time");
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                            SystemClock.elapsedRealtime() + 5000, redtime * 1000,
                            alarmIntent);
                }
            }
        }
    }

    static public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
            context.stopService(new Intent(context, AudioRecorder.class));
        }
    }

    static public void cancelRAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmManager != null) {
            Log.v("v", "com");
            alarmManager.cancel(alarmIntent);

            context.stopService(new Intent(context, AudioRecorder.class));
            if (start == 1) {
                AudioRecorder.stopRec();
                start = 0;
            }
        }
    }

    static public void set_msg(String msg) {
        sig_msg = msg;

    }

    static public String get_msg() {
        return sig_msg;
    }

    // Get Location
}
