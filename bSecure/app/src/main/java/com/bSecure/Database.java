package com.bSecure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "bSecure";
	private static final String TABLE_GCONTACT = "green";
	private static final String TABLE_YCONTACT = "yellow";
	private static final String TABLE_RCONTACT = "red";
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PHONENO ="phoneno";
	private static final String KEY_EMAIL_ID ="emailId";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLEG = "CREATE TABLE " + TABLE_GCONTACT + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT, " + KEY_PHONENO + " TEXT)";
		db.execSQL(CREATE_TABLEG);

		String CREATE_TABLEY = "CREATE TABLE " + TABLE_YCONTACT + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT, " + KEY_PHONENO + " TEXT)";
		db.execSQL(CREATE_TABLEY);

		String CREATE_TABLER = "CREATE TABLE " + TABLE_RCONTACT + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT, " + KEY_PHONENO + " TEXT, " + KEY_EMAIL_ID + " TEXT)";
		db.execSQL(CREATE_TABLER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GCONTACT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_YCONTACT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RCONTACT);
		onCreate(db);
	}

	public void updateEmailId(String newEmailId, String phoneNo){
		SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_EMAIL_ID, newEmailId);
			db.update(TABLE_RCONTACT, values, KEY_PHONENO + " = '" + phoneNo + "'", null);
			db.close();
	}

	public void addContact(BSecureData data, String sig) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		if (sig.equals("green")) {
			values.put(KEY_NAME, data.getName1());
			values.put(KEY_PHONENO, data.getPhone_no());
			db.insert(TABLE_GCONTACT, null, values);
		}
		if (sig.equals("yellow")) {
			values.put(KEY_NAME, data.getName1());
			values.put(KEY_PHONENO, data.getPhone_no());
			db.insert(TABLE_YCONTACT, null, values);
		}
		if (sig.equals("red")) {
			values.put(KEY_NAME, data.getName1());
			values.put(KEY_PHONENO, data.getPhone_no());
			values.put(KEY_EMAIL_ID, data.getEmailId());
			db.insert(TABLE_RCONTACT, null, values);
		}
	}

	public Cursor getContact(String sig) throws SQLException {

		String table = null;
		Cursor mCursor =null;
		SQLiteDatabase db = this.getWritableDatabase();
		if (sig.equals("green")) {
			table = TABLE_GCONTACT;
			 mCursor = db.query(true, table,
					new String[] { KEY_ID, KEY_NAME, KEY_PHONENO}, null, null, null, null,
					null, null);
		}
		if (sig.equals("yellow")) {
			table = TABLE_YCONTACT;
			mCursor = db.query(true, table,
					new String[] { KEY_ID, KEY_NAME, KEY_PHONENO}, null, null, null, null,
					null, null);
		}
		if (sig.equals("red")) {
			table = TABLE_RCONTACT;
			mCursor = db.query(true, table,
					new String[] { KEY_ID, KEY_NAME, KEY_PHONENO,KEY_EMAIL_ID}, null, null, null, null,
					null, null);
		}

		if (mCursor != null) {

			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public void deletContact(String id, String sig) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.v("id", id);
		if (sig.equals("green")) {
			db.delete(TABLE_GCONTACT, KEY_ID + " = ?", new String[] { id });
			db.close();
		}
		if (sig.equals("yellow")) {
			db.delete(TABLE_YCONTACT, KEY_ID + " = ?", new String[] { id });
			db.close();
		}
		if (sig.equals("red")) {
			db.delete(TABLE_RCONTACT, KEY_ID + " = ?", new String[] { id });
			db.close();
		}
	}
}
