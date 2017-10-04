package com.bSecure;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class WidgetSettings extends FragmentActivity implements ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;
	static Context con;
	static Intent intent;
	static Intent intentg;
	static Intent intenty;
	static Intent intentr;
	static ImageButton buttongreen;
	static ImageButton buttonyellow;
	static ImageButton buttonred;
	static TextView textgreen, textyellow, textred;
	static SharedPreferences mode;
	static SharedPreferences.Editor editor;
	static boolean isWidgetHelpShown = false;
	public static final String PREFS_NAME = "MyPrefs";

	static WidgetReceiver wm = new WidgetReceiver();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mode = getSharedPreferences(PREFS_NAME, 0);
		editor = mode.edit();
		try {
			Intent intent = new Intent(getApplicationContext(), AudioRecorder.class);
			// add infos for the service  which file to download and where to
			// store
			intent.putExtra("audio", "loc");

			startService(intent);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), "not found",
					Toast.LENGTH_SHORT).show();
		}
		if(!isWidgetHelpShown) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Hello User");
			builder.setMessage(R.string.help);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					isWidgetHelpShown = true;
				}
			}).show();
		}
		con = this;
		intent = getIntent();

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {

			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		intentg = new Intent(WidgetSettings.this, WidgetReceiver.class);
		intentg.setAction("CHANGE_PICTUREG");

		intenty = new Intent(WidgetSettings.this, WidgetReceiver.class);
		intenty.setAction("CHANGE_PICTUREY");

		intentr = new Intent(WidgetSettings.this, WidgetReceiver.class);
		intentr.setAction("CHANGE_PICTURER");

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:

				return new greenFragment();
			case 1:

				return new yellowFragment();

			case 2:

				return new redFragment();

			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title = null;
			if (position == 0) {
				title = "GREEN";
			}
			if (position == 1) {
				title = "YELLOW";
			}
			if (position == 2) {
				title = "RED";
			}
			return title;
		}
	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class greenFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			final View rootView = inflater.inflate(R.layout.fragment_green,
					container, false);
			textgreen = (TextView) rootView.findViewById(R.id.txtgreenabt);
			textgreen.setVisibility(rootView.INVISIBLE);
			buttongreen = (ImageButton) rootView.findViewById(R.id.imggreen);
			

			if (!mode.getBoolean("greenimage", false)) {
				buttongreen.setBackgroundResource(R.drawable.greenicon);

			} else {
				buttongreen.setBackgroundResource(R.drawable.pausegreen);

			}

			rootView.findViewById(R.id.imggreen).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {

							if (mode.getBoolean("greenimage", false)) {
								Log.v("in App image green true",
										"" + mode.getBoolean("greenimage", false));

								buttongreen.setBackgroundResource(R.drawable.greenicon);

							} else {

								if (mode.getBoolean("yellowimage", false)) {

									buttonyellow.setBackgroundResource(R.drawable.yellowicon);

								}
								if (mode.getBoolean("redimage", false)) {
									buttonred.setBackgroundResource(R.drawable.redicon);
								}

								buttongreen.setBackgroundResource(R.drawable.pausegreen);
							}
							wm.onReceive(con, intentg);
						}

					});

			rootView.findViewById(R.id.btnabtgrn).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if(!textgreen.isShown()){
								Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.pull_in_from_left);
								textgreen.startAnimation(animation);
							}
							textgreen.setVisibility(rootView.VISIBLE);
							buttongreen.setVisibility(rootView.INVISIBLE);

						}
					});

			rootView.findViewById(R.id.btnhomegrn).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(textgreen.isShown()) {
								Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.push_out_to_left);
								textgreen.startAnimation(animation);
							}
							textgreen.setVisibility(rootView.INVISIBLE);
							final Handler handler = new Handler();
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									buttongreen.setVisibility(rootView.VISIBLE);
								}
							}, 100);
						}
					});

			rootView.findViewById(R.id.btnsettinggrn).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(con, ApplicationSettings.class);
							i.putExtra("signal", "green");
							startActivity(i);
						}
					});
			return rootView;
		}
	}

	public static class yellowFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_yellow,
					container, false);
			textyellow = (TextView) rootView.findViewById(R.id.txtyellowabt);
			textyellow.setVisibility(rootView.INVISIBLE);
			buttonyellow = (ImageButton) rootView.findViewById(R.id.imgylw);
			if (!mode.getBoolean("yellowimage", false)) {
				buttonyellow.setBackgroundResource(R.drawable.yellowicon);

			} else {
				buttonyellow.setBackgroundResource(R.drawable.pauseyellow);

			}

			// Demonstration of a collection-browsing activity.
			rootView.findViewById(R.id.imgylw).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {

							if (mode.getBoolean("yellowimage", false)) {

								buttonyellow.setBackgroundResource(R.drawable.yellowicon);
							} else {

								Log.v("App image yellow false", "" + mode.getBoolean("yellowimage", false));
								if (mode.getBoolean("greenimage", false)) {

									buttongreen.setBackgroundResource(R.drawable.greenicon);

								}
								if (mode.getBoolean("redimage", false)) {
									buttonred.setBackgroundResource(R.drawable.redicon);
								}

								buttonyellow.setBackgroundResource(R.drawable.pauseyellow);
							}
							wm.onReceive(con, intenty);
						}

					});

			rootView.findViewById(R.id.btnabtylw).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(!textyellow.isShown()){
								Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.pull_in_from_left);
								textyellow.startAnimation(animation);
							}
							textyellow.setVisibility(rootView.VISIBLE);
							buttonyellow.setVisibility(rootView.INVISIBLE);
							textgreen.setVisibility(rootView.INVISIBLE);
							textred.setVisibility(rootView.INVISIBLE);
						}
					});

			rootView.findViewById(R.id.btnhomeylw).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(textyellow.isShown()) {
								Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.push_out_to_left);
								textyellow.startAnimation(animation);
							}
							textyellow.setVisibility(rootView.INVISIBLE);
							final Handler handler = new Handler();
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									buttonyellow.setVisibility(rootView.VISIBLE);
								}
							}, 100);
						}
					});

			rootView.findViewById(R.id.btnsettingylw).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(con, ApplicationSettings.class);
							i.putExtra("signal", "yellow");
							startActivity(i);

						}
					});

			return rootView;
		}
	}

	public static class redFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_red,
					container, false);
			textred = (TextView) rootView.findViewById(R.id.txtredabt);
			textred.setVisibility(rootView.INVISIBLE);
			buttonred = (ImageButton) rootView.findViewById(R.id.imgred);
			if (!mode.getBoolean("redimage", false)) {
				buttonred.setBackgroundResource(R.drawable.redicon);

			} else {
				buttonred.setBackgroundResource(R.drawable.pause);

			}

			// Demonstration of a collection-browsing activity.
			rootView.findViewById(R.id.imgred).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (mode.getBoolean("redimage", false)) {
								Log.v("in App image red true",
										"" + mode.getBoolean("redimage", false));

								buttonred.setBackgroundResource(R.drawable.redicon);
							} else {

								if (mode.getBoolean("yellowimage", false)) {

									buttonyellow.setBackgroundResource(R.drawable.yellowicon);

								}
								if (mode.getBoolean("greenimage", false)) {
									buttongreen.setBackgroundResource(R.drawable.greenicon);
								}

								buttonred.setBackgroundResource(R.drawable.pause);
							}
							wm.onReceive(con, intentr);
						}
					});

			rootView.findViewById(R.id.btnabtred).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(!textred.isShown()){
								Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.pull_in_from_left);
								textred.startAnimation(animation);
							}
							textred.setVisibility(rootView.VISIBLE);
							buttonred.setVisibility(rootView.INVISIBLE);
							textgreen.setVisibility(rootView.INVISIBLE);
						}
					});

			rootView.findViewById(R.id.btnhomered).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(textred.isShown()) {
								Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.push_out_to_left);
								textred.startAnimation(animation);
							}
							textred.setVisibility(rootView.INVISIBLE);
							final Handler handler = new Handler();
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									buttonred.setVisibility(rootView.VISIBLE);
								}
							}, 100);
						}
					});

			rootView.findViewById(R.id.btnsettingred).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(con, ApplicationSettings.class);
							i.putExtra("signal", "red");
							startActivity(i);
						}
					});
			return rootView;
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						WidgetSettings.this.finish();
					}
				})
				.setNegativeButton("No", null)
				.show();
	}
}
