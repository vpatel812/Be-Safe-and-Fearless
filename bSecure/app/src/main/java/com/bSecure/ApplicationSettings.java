package com.bSecure;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ApplicationSettings extends FragmentActivity {
    Context con = this;
    SharedPreferences appSettingSharedPref, prefs;
    SharedPreferences.Editor edit, sedit;
    Button btnsave, btnsetmsg, btnh, btnm, bt, btnShake;
    Integer sec = 0, secm, mh, gsec, ysec, rsec, hour = 0, minute = 0, th, tm;
    String phoneNumber, gvalue, yvalue, rvalue, total;
    ListView lv;
    ArrayList<String> stringArrayList;
    private static ArrayList<MyContactData> arrlstContactData = new ArrayList<MyContactData>();
    CustomAdapter adapter;
    int xc = 0;
    Database db;
    Cursor phoneCursor;
    Activity context;

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_left);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.application_settings);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold); //to add animation
        context = this;
        db = new Database(con);
        bt = (Button) findViewById(R.id.btnaddcnt);
        lv = (ListView) findViewById(R.id.listViewContact);
        btnsave = (Button) findViewById(R.id.btnsaveg);
        btnsetmsg = (Button) findViewById(R.id.btnsetmsg);
        btnh = (Button) findViewById(R.id.btnhour);
        btnm = (Button) findViewById(R.id.btnminute);
        btnShake = (Button) findViewById(R.id.btnshake);

        Intent i = getIntent();
        lv.setAdapter(adapter);

        appSettingSharedPref = getSharedPreferences("signal", 0);
        String msg = i.getStringExtra("signal");
        if (msg.equals("green")) {
            btnShake.setEnabled(false);
        }
        if (msg.equals("yellow")) {
            btnShake.setEnabled(false);
        }
        int flg = i.getFlags();
        Log.v("flag", String.valueOf(flg));
        Log.v("msg", msg);
        adapter = new CustomAdapter(context, R.layout.listview_contacts, arrlstContactData, lv, msg);
        edit = appSettingSharedPref.edit();
        prefs = getSharedPreferences("shake", 0);
        sedit = prefs.edit();
        String sp = prefs.getString("shakeon", null);
        if (sp != null) {
            btnShake.setText(prefs.getString("shakeon", ""));
        }
        btnShake.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (prefs.getBoolean("shake", false) == false) {
                    sedit.putBoolean("shake", true);
                    sedit.putString("shakeon", "Shake Disable");
                    sedit.commit();
                    btnShake.setText(prefs.getString("shakeon", "Shake Disable"));
                    startService(new Intent(ApplicationSettings.this,
                            ShakeWakeupService.class));
                } else {
                    sedit.putBoolean("shake", false);
                    sedit.putString("shakeon", "Shake Enable");
                    sedit.commit();
                    btnShake.setText("Shake Enable");
                    stopService(new Intent(ApplicationSettings.this,
                            ShakeWakeupService.class));

                }
            }
        });

        // Green Signal
        if (msg.equals("green")) {
            listcall("green");
            int GHour = appSettingSharedPref.getInt("GHour", 0);

            if (GHour == 0) {
                btnh.setText("Set Hour");
            } else {
                btnh.setText(appSettingSharedPref.getInt("GHour", 0) + " " + "Hour");
            }

            int GMin = appSettingSharedPref.getInt("GMin", 0);

            if (GMin == 0) {
                btnm.setText("Set Minute");
            } else {
                btnm.setText(appSettingSharedPref.getInt("GMin", 0) + " " + "Minute");
            }
            btnsetmsg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    setMessage("green");
                }
            });
            bt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    int i = arrlstContactData.size();
                    if (i == 3 || i > 3) {
                        Toast.makeText(getApplicationContext(),
                                "Maximum 3 Added !", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                }
            });

            btnh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    openDialog("hour", "green");

                }
            });

            btnm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    openDialog("minute", "green");

                }
            });

            btnsave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (arrlstContactData.size() > 0) {

                        gsec = getTime("green");
                        if (gsec == 0) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "You select Application Default time for send Message!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        edit.putInt("gtime", gsec);
                        edit.commit();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "please add contact!", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            });

        }

        // Yellow Signal
        if (msg.equals("yellow")) {
            listcall("yellow");
            int YHour = appSettingSharedPref.getInt("YHour", 0);

            if (YHour == 0) {
                btnh.setText("Set Hour");
            } else {
                btnh.setText(appSettingSharedPref.getInt("YHour", 0) + " " + "Hour");
            }

            int YMin = appSettingSharedPref.getInt("YMin", 0);

            if (YMin == 0) {
                btnm.setText("Set Minute");
            } else {
                btnm.setText(appSettingSharedPref.getInt("YMin", 0) + " " + "Minute");
            }
            btnsetmsg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    setMessage("yellow");
                }
            });

            bt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    int i = arrlstContactData.size();
                    if (i == 5 || i > 5) {
                        Toast.makeText(getApplicationContext(),
                                "Maximum 5 Added !", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, 4);
                    }
                }
            });

            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        final int index, long arg3) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder builder = new AlertDialog.Builder(con);
                    builder.setTitle("Hello User");
                    builder.setMessage("Contact will be deleted !");

                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    String in = arrlstContactData.get(index).strID;
                                    db.deletContact(in, "yellow");
                                    listcall("yellow");

                                }
                            })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                        }
                                    }).show();

                }

            });
            btnh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    openDialog("hour", "yellow");
                }
            });

            btnm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    openDialog("minute", "yellow");

                }
            });

            btnsave.setOnClickListener(new View.OnClickListener() {
                // edttime.getSelectedItem().toString();
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (arrlstContactData.size() > 0) {

                        ysec = getTime("yellow");
                        if (ysec == 0) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "You select Application Default time for send Message!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        edit.putInt("ytime", ysec);
                        edit.commit();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "please add contact!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }

        // Red Signal
        if (msg.equals("red")) {
            listcall("red");
            int RHour = appSettingSharedPref.getInt("RHour", 0);

            if (RHour == 0) {
                btnh.setText("Set Hour");
            } else {
                btnh.setText(appSettingSharedPref.getInt("RHour", 0) + " " + "Hour");
            }

            int RMin = appSettingSharedPref.getInt("RMin", 0);

            if (RMin == 0) {
                btnm.setText("Set Minute");
            } else {
                btnm.setText(appSettingSharedPref.getInt("RMin", 0) + " " + "Minute");
            }
            Log.v("msg", "red");
            btnsetmsg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    setMessage("red");
                }
            });
            bt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    int i = arrlstContactData.size();
                    if (i == 7 || i > 7) {
                        Toast.makeText(getApplicationContext(),
                                "Maximum 7 Added !", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, 6);
                    }
                }
            });

            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        final int index, long arg3) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder builder = new AlertDialog.Builder(con);
                    builder.setTitle("Hello User");
                    builder.setMessage("Contact will be deleted !");

                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    String in = arrlstContactData.get(index).strID;
                                    db.deletContact(in, "red");

                                    listcall("red");

                                }
                            })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                        }
                                    }).show();

                }

            });

            btnh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    openDialog("hour", "red");
                }
            });

            btnm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    openDialog("minute", "red");

                }
            });
            btnsave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (arrlstContactData.size() > 0) {
                        if (checkIfContactHasEmailId()) {
                            rsec = getTime("red");
                            if (rsec == 0) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "You select Application Default time for send Message!",
                                        Toast.LENGTH_LONG).show();
                            }
                            edit.putInt("rtime", rsec);
                            edit.commit();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Email ID is missing for atleast one contact", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "please add contact!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        }
    }

    public static ArrayList<MyContactData> getArrlstContactData() {
        return arrlstContactData;
    }

    public static void setArrlstContactData(ArrayList<MyContactData> arrlstContactData) {
        ApplicationSettings.arrlstContactData = arrlstContactData;
    }

    private int getTime(String sig) {

        String m = minute.toString();

        if (sig.equals("green")) {
            hour = appSettingSharedPref.getInt("GHour", 0);
            minute = appSettingSharedPref.getInt("GMin", 0);
        }
        if (sig.equals("yellow")) {
            hour = appSettingSharedPref.getInt("YHour", 0);
            minute = appSettingSharedPref.getInt("YMin", 0);
        }
        if (sig.equals("red")) {
            hour = appSettingSharedPref.getInt("RHour", 0);
            minute = appSettingSharedPref.getInt("RMin", 0);
        }

        if (hour > 0) {
            Log.v("h", hour.toString());
            mh = hour * 60;
            Log.v("h", mh.toString());
            sec = mh * 60;
        } else {
            sec = 0;
        }

        if (minute > 0) {
            Log.v("m", minute.toString());
            secm = minute * 60;
            Log.v("sm", secm.toString());
            sec += secm;
            Log.v("s", sec.toString());
        } else {
            sec += 0;
        }
        String s = sec.toString();
        if(s!= null && !s.equals("0")){
            Toast.makeText(con, s, Toast.LENGTH_SHORT).show();
        }

        return sec;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (2):
                setcnt(reqCode, resultCode, data, "green");
                break;
            case (4):
                setcnt(reqCode, resultCode, data, "yellow");
                break;
            case (6):
                setcnt(reqCode, resultCode, data, "red");
                break;
        }
    }

    private void setcnt(int reqCode, int resultCode, Intent data, String signal) {
        String sig = null;
        String name = "", contact_id = "", contact_emailId = "";

        if (resultCode == Activity.RESULT_OK) {
            String s = data.getData().toString();
            Uri contactData = data.getData();

            @SuppressWarnings("deprecation")
            Cursor c = managedQuery(contactData, null, null, null, null);

            if (c.moveToFirst()) {
                // other data is available for the Contact. I have decided
                // to only get the name of the Contact.
                try {
                    name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.v("nam", name);
                    int hasPhoneNumber = Integer
                            .parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                    contact_id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                    Log.v("hn", String.valueOf(hasPhoneNumber));
                    if (hasPhoneNumber > 0) {

                        // Query and loop for every phone number of the contact

                        phoneCursor = managedQuery(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + " = ?", new String[]{contact_id},
                                null);

                        if (phoneCursor.moveToFirst()) {
                            phoneNumber = phoneCursor
                                    .getString(phoneCursor
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Log.v("Phone number:", phoneNumber);

                        }

                        try {
                            if (signal.equals("green")) {
                                Log.v("d", "c");
                                db.addContact(new BSecureData(name, phoneNumber, contact_emailId),
                                        "green");
                                sig = "green";
                            }
                            if (signal.equals("yellow")) {
                                Log.v("d", "c");
                                db.addContact(new BSecureData(name, phoneNumber, contact_emailId),
                                        "yellow");
                                sig = "yellow";
                            }
                            if (signal.equals("red")) {
                                Log.v("d", "c");
                                db.addContact(new BSecureData(name, phoneNumber, contact_emailId), "red");
                                sig = "red";
                            }
                        } catch (Exception e) {
                            Toast.makeText(ApplicationSettings.this,
                                    "Date Insert Failed", Toast.LENGTH_SHORT)
                                    .show();
                            Log.v("data", "fail");
                        }

                        Toast.makeText(getApplicationContext(), name,
                                Toast.LENGTH_SHORT).show();

                    } else {
                        xc = 1;
                        Log.v("no", "number");
                        Toast.makeText(getApplicationContext(),
                                "No number Found !", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    xc = 1;
                    Log.v("catch", String.valueOf(xc));
                    Log.v("no", "number");
                    Toast.makeText(getApplicationContext(),
                            "No number Found !", Toast.LENGTH_SHORT).show();
                }
            }

        }

        Log.v("acatch", String.valueOf(xc));
        if (xc == 1) {
            Toast.makeText(getApplicationContext(), "No number Found !",
                    Toast.LENGTH_SHORT).show();
            xc = 0;
            Log.v("concatch", String.valueOf(xc));
        } else {
            MyContactData str = null;
            Cursor c1 = db.getContact(signal);

            if (c1.getCount() > 0) {
                if (c1.moveToLast()) {
                    int s = arrlstContactData.size() - 1;
                    if (s >= 0) {
                        str = arrlstContactData.get(s);
                    }
                    if (signal.equals("red")) {
                        contact_emailId = c1.getString(3);
                    }
                    if (Integer.parseInt(c1.getString(0)) > 0) {
                        if (s >= 0) {
                            if ((str != null) && (str.strID.equals(c1.getString(0)) && str.strNumber.equals(c1.getString(1)))) {
                                Log.v("no", "no selected");
                            } else {
                                MyContactData tempContact = new MyContactData(name, phoneNumber, contact_id, contact_emailId);
                                arrlstContactData.add(tempContact);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            MyContactData tempContact = new MyContactData(name, phoneNumber, contact_id, contact_emailId);
                            arrlstContactData.add(tempContact);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Add only Phone contact", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            } else {
                Log.v("data", "not found");
            }
        }

    }

    // Number Picker
    private void openDialog(final String tim, final String sig) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("Hello User");
        if (tim.equals("hour")) {
            builder.setMessage("Set Hour");
        }

        if (tim.equals("minute")) {
            builder.setMessage("Set Minute");
        }

        final NumberPicker np = new NumberPicker(con);
        np.setId(0);
        np.setMinValue(0);

        if (tim.equals("hour")) {
            np.setMaxValue(12);
        }
        if (tim.equals("minute")) {
            np.setMaxValue(59);
        }

        builder.setView(np);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                if (tim.equals("hour")) {

                    hour = np.getValue();
                    if (sig.equals("green")) {

                        edit.putInt("GHour", hour);
                    }
                    if (sig.equals("yellow")) {

                        edit.putInt("YHour", hour);
                    }
                    if (sig.equals("red")) {

                        edit.putInt("RHour", hour);
                    }
                    if (hour > 0) {
                        btnh.setText(hour.toString() + " " + "Hour");
                    } else {
                        btnh.setText("Set Hour");
                    }

                }
                edit.commit();

                if (tim.equals("minute")) {
                    minute = np.getValue();
                    if (sig.equals("green")) {
                        edit.putInt("GMin", minute);
                    }
                    if (sig.equals("yellow")) {
                        edit.putInt("YMin", minute);
                    }
                    if (sig.equals("red")) {
                        edit.putInt("RMin", minute);
                    }
                    if (minute > 0) {
                        btnm.setText(minute.toString() + " " + "Minute");
                    } else {
                        btnm.setText("Set Minute");
                    }
                }

                edit.commit();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (tim.equals("hour")) {
                    // hour=0;
                    if (sig.equals("green")) {
                        hour = appSettingSharedPref.getInt("GHour", 0);
                        edit.putInt("GHour", hour);
                    }
                    if (sig.equals("yellow")) {
                        hour = appSettingSharedPref.getInt("YHour", 0);
                        edit.putInt("YHour", hour);
                    }
                    if (sig.equals("red")) {
                        hour = appSettingSharedPref.getInt("RHour", 0);
                        edit.putInt("RHour", hour);
                    }

                }

                if (tim.equals("minute")) {
                    if (sig.equals("green")) {
                        minute = appSettingSharedPref.getInt("GMin", 0);
                        edit.putInt("GMin", minute);
                    }
                    if (sig.equals("yellow")) {
                        minute = appSettingSharedPref.getInt("YMin", 0);
                        edit.putInt("YMin", minute);
                    }
                    if (sig.equals("red")) {
                        minute = appSettingSharedPref.getInt("RMin", 0);
                        edit.putInt("RMin", minute);
                    }
                }
                edit.commit();
            }
        }).show();

    }

    // Dialog For Set Message
    private void setMessage(String signal) {

        if (signal.equals("green")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("Hello User");
            builder.setMessage("Set Green Signal Message");

            final EditText input = new EditText(con);
            input.setId(0);
            input.setText(appSettingSharedPref.getString("gmsg", ""));
            builder.setView(input);

            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            gvalue = input.getText().toString();
                            edit.putString("gmsg", gvalue);
                            Log.d("TAG", "User name: " + gvalue);
                            edit.commit();
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            }).show();

        }

        if (signal.equals("yellow")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("Hello User");
            builder.setMessage("Set Yellow Signal Message");

            final EditText input = new EditText(con);
            input.setId(0);
            input.setText(appSettingSharedPref.getString("ymsg", ""));
            builder.setView(input);

            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            yvalue = input.getText().toString();
                            edit.putString("ymsg", yvalue);
                            Log.d("TAG", "User name: " + yvalue);
                            edit.commit();
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            }).show();

        }

        if (signal.equals("red")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("Hello User");
            builder.setMessage("Set Red Signal Message");

            final EditText input = new EditText(con);
            input.setId(0);
            input.setText(appSettingSharedPref.getString("rmsg", ""));
            builder.setView(input);

            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            rvalue = input.getText().toString();
                            edit.putString("rmsg", rvalue);
                            Log.d("TAG", "User name: " + rvalue);
                            edit.commit();
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            }).show();

        }
    }

    public void listcall(String signal) {
        Cursor c1 = null;
        arrlstContactData = new ArrayList<MyContactData>();
        adapter = new CustomAdapter(context, R.layout.listview_contacts, arrlstContactData, lv, signal);
        lv.setAdapter(adapter);
        Log.v("sur", "com");
        db = new Database(con);
        Log.v("sur", "com");
        if (signal.equals("green")) {
            Log.v("sur", "comg");
            c1 = db.getContact("green");
        }
        if (signal.equals("yellow")) {
            c1 = db.getContact("yellow");
        }
        if (signal.equals("red")) {
            c1 = db.getContact("red");
        }
        String s = c1.toString();
        Log.v("c1", s);
        if (c1.getCount() > 0) {
            if (c1.moveToLast()) {
                for (c1.moveToFirst(); !c1.isAfterLast(); c1.moveToNext()) {

                    if (Integer.parseInt(c1.getString(0)) > 0) {
                        MyContactData tempMyContactData;
                        if (signal.equals("red")) {
                            tempMyContactData = getContactDetails(c1.getString(0), c1.getString(1), c1.getString(2), c1.getString(3));
                        } else {
                            tempMyContactData = getContactDetails(c1.getString(0), c1.getString(1), c1.getString(2), null);
                        }
                        arrlstContactData.add(tempMyContactData);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.v("list", "not found!");
                    }

                }

            }
        } else {
            Log.v("data", "not found");
        }
    }

    MyContactData getContactDetails(String strID, String strName, String strPhoneno, String strEmailId) {
        Log.d("Name", strName);
        Log.d("ID", strID);
        Log.d("Number", strPhoneno);
        if (strEmailId != null) {
            Log.d("Email ID", strEmailId);
        }
        MyContactData tempContact = new MyContactData(strName, strPhoneno, strID, strEmailId);
        return tempContact;
    }

    class CustomAdapter extends ArrayAdapter<MyContactData> {
        Context context;
        Activity actSel;
        String strSignal;
        private LayoutInflater inflater = null;

        public CustomAdapter(Activity actSelected, int resource, ArrayList<MyContactData> arrlstAllContacts, ListView lstContacts, String strSignal) {
            super(actSelected, resource);
            this.strSignal = strSignal;
            context = actSelected;
            actSel = actSelected;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrlstContactData.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            ImageView img;
            TextView lblName;
            TextView lblPhoneNo;
            Button buttonAddEmail;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();
            final View rowView;
            final MyContactData curContact = arrlstContactData.get(position);
            rowView = inflater.inflate(R.layout.listview_contacts, null);
            if (position % 2 == 1) {
                rowView.setBackgroundColor(Color.parseColor("#F3F3F1"));
            } else {
                rowView.setBackgroundColor(Color.WHITE);
            }
            holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
            holder.lblName = (TextView) rowView.findViewById(R.id.contact_name);
            holder.lblPhoneNo = (TextView) rowView.findViewById(R.id.contact_number);
            holder.lblName.setText(curContact.strName);
            holder.lblPhoneNo.setText(curContact.strNumber);
            holder.buttonAddEmail = (Button) rowView.findViewById(R.id.buttonAddEmail);
            if (strSignal.equals("red")) {
                holder.buttonAddEmail.setVisibility(View.VISIBLE);
                holder.buttonAddEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationSettings.this);
                        final EditText emailId = new EditText(con);
                        String tempEmail = arrlstContactData.get(position).strEmailId;
                        emailId.setText((tempEmail != null) ? tempEmail : "");
                        emailId.setHint("Email ID");
                        emailId.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        builder.setView(emailId);
                        builder.setCancelable(false)
                                .setMessage("Please add email Id")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        theButton.setOnClickListener(new CustomListener(alertDialog, emailId, position, db));
                    }
                });
            } else {
                holder.buttonAddEmail.setVisibility(View.INVISIBLE);
            }

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, curContact.strName + "\t\tPhone No.: " + curContact.strNumber, Toast.LENGTH_LONG).show();
                    // TODO Auto-generated method stub
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Hello User");
                    builder.setMessage("Contact will be deleted !");

                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    String in = curContact.strID;
                                    db.deletContact(in, strSignal);
                                    listcall(strSignal);

                                }
                            })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                        }
                                    }).show();
                }
            });


            return rowView;
        }

    }

    public boolean checkIfContactHasEmailId() {
        boolean contactHasEmailId = true;
        if (arrlstContactData != null && !arrlstContactData.isEmpty()) {
            for (MyContactData myContactData : arrlstContactData) {
                if (myContactData.strEmailId.equals("")) {
                    contactHasEmailId = false;
                    break;
                }
            }
        }
        return contactHasEmailId;
    }
}


class MyContactData {
    String strName;
    String strNumber;
    String strID;
    String strEmailId;

    MyContactData(String strName, String strNumber, String strID, String strEmailId) {
        this.strName = strName;
        this.strNumber = strNumber;
        this.strID = strID;
        this.strEmailId = strEmailId;
    }
}



