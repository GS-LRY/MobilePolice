package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
	private static String DB_NAME = "Police.db";// 数据库名称
	public static String TABLE_NAME_PoliceInformation = "tbl_policeinfo";// 警员表名
	public static String TABLE_NAME_FingerPrintTemplate = "tbl_fingerprinttemplate";// 指纹特征表名
	public static String TABLE_NAME_Normal = "tbl_normal";// 核录身份信息记录表
	public static String TABLE_NAME_Escaped = "tbl_escaped";//在逃人员表

	public MyHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create table
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME_PoliceInformation + " ( "
				+ "id INTEGER PRIMARY KEY," + "username TEXT,"
				+ "password TEXT);";
		db.execSQL(sql);

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME_FingerPrintTemplate
				+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,mIDNo TEXT,"
				+ "Right_Thumb_Template TEXT,Right_ForeFinger_Template TEXT,"
				+ "Right_MiddleFinger_Template TEXT,Right_RingFinger_Template TEXT,"
				+ "Right_LittleFinger_Template TEXT,Left_Thumb_Template TEXT,"
				+ "Left_ForeFinger_Template TEXT,Left_MiddleFinger_Template TEXT,"
				+ "Left_RingFinger_Template TEXT,Left_LittleFinger_Template TEXT);");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME_Normal
				+ " ( id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " persionId TEXT, personName TEXT , addressCode TEXT, "
				+ " addressName TEXT , addressGPS TEXT , commitTime TEXT , infoSubmit INTEGER ,personFp INTEGER ,"
				+ " userId INTEGER );");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_Escaped + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"XM TEXT, XB TEXT, SFZH TEXT, ZDRYLBBJ TEXT, ZDRYXL TEXT," +
				"LADW TEXT, NRBJZDRYKSJ TEXT, HJDQH TEXT, HJDXZ TEXT,XZDQH TEXT,XZDXZ TEXT, ZJLASJ TEXT)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PoliceInformation);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FingerPrintTemplate);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Normal);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Escaped);
		onCreate(db);
	}

}
