package com.bSecure;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

public class MessageSender extends IntentService implements ConnectionCallbacks,
        OnConnectionFailedListener {

    String msg, gmsg, ymsg, rmsg;
    Context con = this;
    SharedPreferences appSettings;

    public MessageSender() {
        super("MessageSender");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        WidgetReceiver wr = new WidgetReceiver();
        Intent inten = new Intent();
        SmsManager sms = SmsManager.getDefault();
        appSettings = con.getSharedPreferences("signal", 0);

        msg = AlarmScheduler.get_msg();
        Database db = new Database(con);

        // Send SMS For Green Signal
        if (msg.equals("green")) {

            LocationFinder.getlc(con, "green");
            gmsg = appSettings.getString("gmsg1", "");
            String s = appSettings.getString("gmsg", "");
            Log.v("gmsg", s + " " + gmsg);
            Toast.makeText(con, gmsg, Toast.LENGTH_SHORT).show();
            Cursor c1 = db.getContact("green");
            if (c1.getCount() > 0) {
                if (c1.moveToLast()) {
                    for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
                        Log.v("con", c1.getString(2));
                        try {
                            Log.v("msg", c1.getString(2));
                            sms.sendTextMessage(c1.getString(2), null, s , null, null);
                        } catch (Exception e) {
                            Log.v("msg", "send fail!");
                        }
                    }
                }
            } else {
                inten.setAction("CHANGE_PICTUREG");
                wr.onReceive(getApplicationContext(), inten);
                Log.v("data", "not found");
            }
            db.close();
        }

        // Send SMS For Yellow Signal
        if (msg.equals("yellow")) {
            LocationFinder.getlc(con, "yellow");
            ymsg = appSettings.getString("ymsg1", "");
            String s = appSettings.getString("ymsg", "");
            Log.v("ymsg", s + " " + ymsg);
            Cursor c1 = db.getContact("yellow");
            if (c1.getCount() > 0) {
                if (c1.moveToLast()) {
                    for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
                        Log.v("con", c1.getString(2));
                        try {
                            sms.sendTextMessage(c1.getString(2), null, s + " "
                                    + ymsg, null, null);
                            Log.v("msg", c1.getString(1));
                        } catch (Exception e) {
                            Log.v("msg", "send fail!");
                        }
                    }
                }
            } else {
                inten.setAction("CHANGE_PICTUREY");
                wr.onReceive(getApplicationContext(), inten);
                Log.v("data", "not found");
            }
            db.close();
        }

        // Send SMS For Red Signal
        if (msg.equals("red")) {
            LocationFinder.getlc(con, "red");
            rmsg = appSettings.getString("rmsg1", "");
            String s = appSettings.getString("rmsg", "");
            Log.v("rmsg", s + " " + rmsg);
            Cursor c1 = db.getContact("red");
            if (c1.getCount() > 0) {
                if (c1.moveToLast()) {
                    for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {
                        Log.v("con", c1.getString(2));
                        try {
                            sms.sendTextMessage(c1.getString(2), null, s + " "
                                    + rmsg, null, null);
                            Log.v("msg", c1.getString(1));
                        } catch (Exception e) {
                            Log.v("msg", "send fail!");
                        }
                    }
                }
            } else {
                inten.setAction("CHANGE_PICTURER");
                wr.onReceive(getApplicationContext(), inten);
                Log.v("data", "not found");
            }
            db.close();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDisconnected() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
    }

}
