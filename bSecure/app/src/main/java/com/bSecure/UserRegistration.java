package com.bSecure;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegistration extends Activity {
	EditText name, mobile_no, email;;
	SharedPreferences regpage;
	SharedPreferences.Editor edit;
	SharedPreferences setting;
	SharedPreferences.Editor editor;
	SharedPreferences prefs;
	Button submitbutton;
	String regexStr = "^[0-9]{10}$";
	String emailstr = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold, R.anim.push_out_to_left);
		super.onPause();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_registration);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold); //to add animation
		SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (!prefs.getBoolean("firstTime", false)) {
			// run your one time code here

			setting = getSharedPreferences("signal", 0);
			editor = setting.edit();
			editor.putString("gmsg", "Hi I am safe !");
			editor.putString("ymsg", "Hi I am in critical situation !");
			editor.putString("rmsg", "Hi I am in trouble, please help me !");
			editor.putInt("gtime", 0);
			editor.putInt("ytime", 0);
			editor.putInt("rtime", 0);
			editor.commit();

			regpage = getSharedPreferences("reg", 0);
			edit = regpage.edit();
			name = (EditText) findViewById(R.id.name);
			mobile_no = (EditText) findViewById(R.id.mobile_no);
			email = (EditText) findViewById(R.id.email);
			submitbutton = (Button) findViewById(R.id.submit);

			submitbutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						if (name.getText().toString().equals("")
								|| mobile_no.getText().toString().equals("")
								|| email.getText().toString().equals("")) {
							Toast.makeText(UserRegistration.this, "Please Fill All Data",
									Toast.LENGTH_SHORT).show();

						} else {
							SharedPreferences.Editor editor = prefs.edit();
							editor.putBoolean("firstTime", true);
							editor.commit();
							if (mobile_no.getText().toString().matches(regexStr)) {
								if (email.getText().toString()
										.matches(emailstr)) {
									edit.putString("name", name.getText()
											.toString());
									edit.putString("mobile", mobile_no.getText()
											.toString());
									edit.putString("email", email.getText()
											.toString());
									edit.commit();
									showMessage();
									Intent intent_first = new Intent(UserRegistration.this,
											WidgetSettings.class);
									startActivity(intent_first);
									finish();
								} else {
									Toast.makeText(UserRegistration.this,
											"Enter Valid Email Address",
											Toast.LENGTH_SHORT).show();
								}
							}

							else {
								Toast.makeText(UserRegistration.this, "Enter Valid Mobile number", Toast.LENGTH_LONG).show();
							}
						}

					} catch (Exception e) {
						System.out.println("" + e.getMessage());
					}

				}
			});

		} else {
			Intent in = new Intent(getApplicationContext(), WidgetSettings.class);
			startActivity(in);
			finish();
		}

	}

	public void showMessage() {
		Toast.makeText(this, "User Registered", Toast.LENGTH_LONG).show();
	}
}
