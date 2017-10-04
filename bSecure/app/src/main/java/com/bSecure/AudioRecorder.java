package com.bSecure;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AudioRecorder extends IntentService implements ConnectionCallbacks,
        OnConnectionFailedListener {

    static MediaRecorder mRecorder;
    static String mFileName;
    static GPSTracker gps;
    static double latitude;
    static double longitude;
    static String gm;
    static SharedPreferences appSettings;
    static SharedPreferences registrationSettings;
    static SharedPreferences.Editor edit;
    static String smsg;
    static String ymsg;
    static String rmsg;

    public AudioRecorder() {
        super("AudioRecorder");
        // TODO Auto-generated constructor stub
    }

    public void startRec() {
        registrationSettings = getSharedPreferences("reg",0);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root + "/bSecure/AUDIO_RECORD");
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "AUD_" + timeStamp + ".3gp");
        mFileName = mediaFile.toString();
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("audioRecord", "failed to create directory");
            }
        }
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("AudioRecordTest", "prepare() failed" + e);
        }
        mRecorder.start();
    }

    public static void stopRec() {
        if(mRecorder!=null){
            mRecorder.stop();
            mRecorder.release();
        }
        //now send recorded audio file through smtp
        sentEmailThroughGmail();
    }

    private static void sentEmailThroughGmail() {
        List<MyContactData> myContactDataList = ApplicationSettings.getArrlstContactData();
        String[] toAddress= new String[myContactDataList.size()];
        int index =0;
        for(MyContactData emailData: myContactDataList){
            toAddress[index] = emailData.strEmailId;
            index = index+1;
        }
        //SharedPreferences regpage = getSharedPreferences("reg", 0);
        if(myContactDataList != null && !myContactDataList.isEmpty()){
            SendMail sm = new SendMail(toAddress,registrationSettings.getString("email",""),mFileName);
            //Executing sendmail to send email
            sm.execute();
        }
    }

    @Override
    protected void onHandleIntent(Intent arg0) {
        // TODO Auto-generated method stub

        String audiosignal = arg0.getStringExtra("audio");

        if (audiosignal.equals("start")) {
            startRec();
        }
        if (audiosignal.equals("loc")) {
            Log.v("service", "start");
            appSettings = getSharedPreferences("signal", 0);
            smsg = appSettings.getString("gmsg", "");
            ymsg = appSettings.getString("ymsg", "");
            rmsg = appSettings.getString("rmsg", "");
            gps = new GPSTracker(getApplicationContext());

            // check if GPS enabled
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                Intent in = new Intent(getApplicationContext(),
                        GPSActivation.class);
                startActivity(in);
            }
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);

                gm = (address + " " + city + " " + country);
            } catch (Exception e) {
                gm = (String.valueOf(latitude) + String.valueOf(longitude));
            }
            Log.v("loc", gm);
            edit = appSettings.edit();
            edit.putString("gmsg1", gm);
            edit.putString("ymsg1", gm);
            edit.putString("rmsg1", gm);
            edit.commit();
            gm = null;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDisconnected() {
        // TODO Auto-generated method stub
    }
}
