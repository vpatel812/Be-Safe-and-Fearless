package com.bSecure;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class LocationFinder {

	static GPSTracker gps;
	static double latitude;
	static double longitude;
	static String gm;
	static SharedPreferences asetting;
	static SharedPreferences.Editor edit;
	static String smsg;


	public LocationFinder() {
		// TODO Auto-generated constructor stub

	}

	static public void getlc(Context c, String sig) {

		asetting = c.getSharedPreferences("signal", 0);

		gps = new GPSTracker(c);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			Intent in = new Intent(c, GPSActivation.class);
			c.startActivity(in);
		}
		Geocoder geocoder;
		List<Address> addresses = null;
		geocoder = new Geocoder(c, Locale.getDefault());
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

		String latlong = String.valueOf(latitude) + String.valueOf(latitude);
		edit = asetting.edit();
		if (sig.equals("green")) {

			edit.putString("gmsg1", gm);

		}
		if (sig.equals("yellow")) {

			edit.putString("ymsg1", gm);

		}
		if (sig.equals("red")) {

			edit.putString("rmsg1", gm);

		}
		edit.commit();
		smsg = null;
		gm = null;
	}
}
