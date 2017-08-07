package com.example.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
	private static String DB_NAME = "Police.db";// 数据库名称
	public static String TABLE_NAME_PoliceInformation = "tbl_policeinfo";// 警员表名
	public static String TABLE_NAME_FingerPrintTemplate = "tbl_fingerprinttemplate";// 指纹特征表名
	public static String TABLE_NAME_Normal = "t_normal";// 核录身份信息记录表
	public static String TABLE_NAME_Escaped = "t_escaped";//在逃人员表
	private Context mContext;
	
	public MyHelper(Context context) {
		super(context, DB_NAME, null, 1);
		mContext = context;
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
				+ " addressName TEXT , addressGPS TEXT , commitTime TEXT , infoSubmit INTEGER ,personFp INTEGER ,comparFp INTEGER,isEscaped INTEGER,"
				+ " userId INTEGER );");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_Escaped + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"XM TEXT, XB TEXT, SFZH TEXT, ZDRYLBBJ TEXT, ZDRYXL TEXT," +
				"LADW TEXT, NRBJZDRYKSJ TEXT, HJDQH TEXT, HJDXZ TEXT,XZDQH TEXT,XZDXZ TEXT, ZJLASJ TEXT)");

//		if(tabIsExist("tbl_escaped", db)){
			executeAssetsSQL(db, "t_escaped.sql");
//		}
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

	/**
     * 读取数据库文件（.sql），并执行sql语句
     * */
	private void executeAssetsSQL(SQLiteDatabase db, String schemaName){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(mContext.getAssets().open(com.example.db.Configuration.DB_PATH+"/"+schemaName)));
			String line;
			String buffer = "";
			while((line = in.readLine())!=null){
				buffer+=line;
				if(line.trim().endsWith(";")){
					db.execSQL(buffer.replace(";", ""));
					buffer = "";
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	 /**
     * 判断是否存在某一张表
     * @param tabName
     * @param db
     * @return
     */
    public boolean tabIsExist(String tabName, SQLiteDatabase db) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from t_escaped where type ='table' and name ='" + tabName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
 
        } catch (Exception e) {
        }
        return result;
    }
}
