package com.bSecure;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

class CustomListener implements View.OnClickListener {
    private final Dialog dialog;
    private EditText emailId;
    private int position;
    private  Database db;

    public CustomListener(Dialog dialog, EditText emailId, int position,Database db) {
        this.dialog = dialog;
        this.emailId = emailId;
        this.position = position;
        this.db = db;
    }
    @Override
    public void onClick(View v) {
        // put your code here
        String mValue = emailId.getText().toString();
        if(isValidEmail(mValue)){
            ArrayList<MyContactData> arrlstContactData = ApplicationSettings.getArrlstContactData();
            ArrayList<MyContactData> tempArrlstContactData = new ArrayList<MyContactData>();
            for(MyContactData myContactData : arrlstContactData){
                MyContactData tempContactData;
                if(arrlstContactData.get(position) == myContactData){
                    tempContactData = new MyContactData(myContactData.strName,myContactData.strNumber,myContactData.strID,mValue);
                    db.updateEmailId(mValue,myContactData.strNumber);
                }else{
                    tempContactData = new MyContactData(myContactData.strName,myContactData.strNumber,myContactData.strID,myContactData.strEmailId);
                }
                tempArrlstContactData.add(tempContactData);
            }
            ApplicationSettings.setArrlstContactData(tempArrlstContactData);
            dialog.dismiss();
        }else{
            emailId.setError("Please enter valid email ID");
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
