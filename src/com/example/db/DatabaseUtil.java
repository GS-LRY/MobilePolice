package com.example.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.bean.FingerPrintTemplate;
import com.example.bean.Person;
import com.example.model.Escaped;
import com.example.model.Normal;

public class DatabaseUtil {
	private MyHelper helper;

	public DatabaseUtil(Context context) {
		super();
		helper = new MyHelper(context);
	}

	public void dropTable(String tableName) {
		helper.getWritableDatabase().execSQL(
				"DROP TABLE IF EXISTS " + tableName);
	}

	public void InitEscapedData(InputStream in) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		String sqlUpdate = null;
		try {
			
			sqlUpdate = readTextFromSDcard(in);
			String[] s = sqlUpdate.split(";");
			//int num = s.length;
			for (int i = 0; i < 10031; i++) {
				if (!TextUtils.isEmpty(s[i])) {
					db.execSQL(s[i]);
				}
			}
			db.setTransactionSuccessful();

			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 按行读取txt
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	private String readTextFromSDcard(InputStream is) throws Exception {
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader bufferedReader = new BufferedReader(reader);
		StringBuffer buffer = new StringBuffer("");
		String str;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
			buffer.append("\n");
		}
		return buffer.toString();
	}

	/**
	 * 插入数据
	 */
	public boolean Insert_PoliceInformation(Person person) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into " + MyHelper.TABLE_NAME_PoliceInformation
				+ "(username,password) values (" + "'" + person.getUsername()
				+ "'," + "'" + person.getPassword() + "'" + ")";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e) {
			Log.e("err", "insert failed");
			return false;
		} finally {
			db.close();
		}
	}

	public boolean Insert_Normal(Normal normal) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "
				+ MyHelper.TABLE_NAME_Normal
				+ "(persionId,personName,addressCode,addressName,addressGPS,commitTime,userId,infoSubmit,personFp,comparFp,isEscaped) values ("
				+ "'" + normal.getPersonid() + "','" + normal.getPersonname()
				+ "' , '" + normal.getAddresscode() + "','"
				+ normal.getAddressname() + "','" + normal.getAddressgps()
				+ "','" + normal.getCommittime() + "'," + normal.getUserid()
				+ ", " + normal.getInfosubmit() + ", " + normal.getPersonfp()
				+ ", " + normal.getComparfp() + ", " + normal.getIsescaped()
				+ ")";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e) {
			Log.e("err", "insert failed");
			return false;
		} finally {
			db.close();
		}
	}

	public boolean Insert_Escaped(Escaped escaped) {
		SQLiteDatabase db = helper.getWritableDatabase();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = escaped.getNrbjzdryksj();
		String nrbjzdryksj = formatter.format(date);
		date = escaped.getZjlasj();
		String zjlasj = formatter.format(date);
		String sql = "insert into "
				+ MyHelper.TABLE_NAME_Escaped
				+ "(XM,XB,SFZH,ZDRYLBBJ,ZDRYXL,LADW,NRBJZDRYKSJ,HJDQH,HJDXZ,XZDQH,XZDXZ,ZJLASJ) values ("
				+ "'" + escaped.getXm() + "','" + escaped.getXb() + "' , '"
				+ escaped.getSfzh() + "','" + escaped.getZdrylbbj() + "','"
				+ escaped.getZdryxl() + "', '" + escaped.getLadw() + "','"
				+ nrbjzdryksj + "', '" + escaped.getHjdqh() + "', '"
				+ escaped.getHjdxz() + "', '" + escaped.getXzdqh() + "', '"
				+ escaped.getXzdxz() + "', '" + zjlasj + "')";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e) {
			Log.e("err", "insert failed");
			return false;
		} finally {
			db.close();
		}
	}

	/**
	 * 更新核录信息提交状态
	 * 
	 * @param id
	 * @return
	 */
	public boolean Update_Normal(int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "update " + MyHelper.TABLE_NAME_Normal
				+ " set infoSubmit = 1 where id = " + id;
		db.execSQL(sql);
		return true;
	}

	public boolean Insert_FingerPrintTemplate(String IDNo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into " + MyHelper.TABLE_NAME_FingerPrintTemplate
				+ "(mIDNo) values (" + " '" + IDNo + "');";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			db.close();
		}
	}

	/**
	 * 更新数据
	 */
	public void Update_PoliceInformation(Person person, int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", person.getUsername());
		values.put("password", person.getPassword());
		int rows = db.update(MyHelper.TABLE_NAME_PoliceInformation, values,
				"id=?", new String[] { id + "" });
		db.close();
	}

	/**
	 * 更新指纹特征数据
	 * 
	 * @param fingerPrintTemplate
	 *            指纹特征
	 * @param whichFinger
	 *            代指更新哪根手指的指纹特征
	 * @param IDNo
	 *            身份证号
	 */
	public void Update_FingerPrintTemplate(String fingerPrintTemplate,
			String whichFinger, String IDNo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(whichFinger, fingerPrintTemplate);
		// String sql =
		// "UPDATE "+MyHelper.TABLE_NAME_FingerPrintTemplate+" SET "+whichFinger+" = '"+fingerPrintTemplate+"' WHERE mIDNo ='"+IDNo+"'";
		try {
			// db.execSQL(sql);
			db.update(MyHelper.TABLE_NAME_FingerPrintTemplate, values,
					"mIDNo = '" + IDNo + "'", null);
		} catch (SQLException e) {
			Log.e("Database", whichFinger + "指纹采集失败");
		} finally {
			db.close();
		}
	}

	/**
	 * 删除数据
	 */
	public void Delete_PoliceInformation(int id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int raw = db.delete(MyHelper.TABLE_NAME_PoliceInformation, "id=?",
				new String[] { id + "" });
		db.close();
	}

	public void Delete_FingerPrintTemplate(String whichfinger, String mIDNo) {
		SQLiteDatabase db = helper.getWritableDatabase();

	}

	/**
	 * 查询所有数据
	 */
	public List<Person> queryAll_PoliceInformation() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Person> list = new ArrayList<Person>();
		Cursor cursor = db.query(MyHelper.TABLE_NAME_PoliceInformation, null,
				null, null, null, null, null);
		while (cursor.moveToNext()) {
			Person person = new Person();
			person.setId(cursor.getInt(cursor.getColumnIndex("id")));
			person.setUsername(cursor.getString(cursor
					.getColumnIndex("username")));
			person.setPassword(cursor.getString(cursor
					.getColumnIndex("password")));
			list.add(person);
		}
		db.close();
		return list;
	}

	/**
	 * 根据身份证号比对在逃人员
	 * 
	 * @param personId
	 * @return
	 * @throws ParseException
	 */
	public Escaped queryEscapedByPersonId(String personId)
			throws ParseException {
		SQLiteDatabase db = helper.getReadableDatabase();
		Escaped escaped = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select * from " + MyHelper.TABLE_NAME_Escaped
				+ " where SFZH = '" + personId + "'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			escaped = new Escaped();
			escaped.setXm(cursor.getString(cursor.getColumnIndex("XM")));
			escaped.setXb(cursor.getString(cursor.getColumnIndex("XB")));
			escaped.setSfzh(cursor.getString(cursor.getColumnIndex("SFZH")));
			escaped.setZdrylbbj(cursor.getString(cursor
					.getColumnIndex("ZDRYLBBJ")));
			escaped.setZdryxl(cursor.getString(cursor.getColumnIndex("ZDRYXL")));
			escaped.setLadw(cursor.getString(cursor.getColumnIndex("LADW")));
			escaped.setNrbjzdryksj(formatter.parse(cursor.getString(cursor
					.getColumnIndex("NRBJZDRYKSJ"))));
			escaped.setHjdqh(cursor.getString(cursor.getColumnIndex("HJDQH")));
			escaped.setHjdxz(cursor.getString(cursor.getColumnIndex("HJDXZ")));
			escaped.setXzdqh(cursor.getString(cursor.getColumnIndex("XZDQH")));
			escaped.setXzdxz(cursor.getString(cursor.getColumnIndex("XZDXZ")));
			escaped.setZjlasj(formatter.parse(cursor.getString(cursor
					.getColumnIndex("ZJLASJ"))));
		}
		return escaped;
	}

	/**
	 * 根据日期查找在逃人员
	 * 
	 * @param personId
	 * @return
	 * @throws ParseException
	 */
	public ArrayList<Escaped> queryEscapedByNrbjzdryksj(Date nrbjzdryksj)
			throws ParseException {
		SQLiteDatabase db = helper.getReadableDatabase();
		Escaped escaped = null;
		ArrayList<Escaped> escapedList = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select * from " + MyHelper.TABLE_NAME_Escaped
				+ " where nrbjzdryksj = '" + nrbjzdryksj + "'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			escaped = new Escaped();
			escaped.setXm(cursor.getString(cursor.getColumnIndex("XM")));
			escaped.setXb(cursor.getString(cursor.getColumnIndex("XB")));
			escaped.setSfzh(cursor.getString(cursor.getColumnIndex("SFZH")));
			escaped.setZdrylbbj(cursor.getString(cursor
					.getColumnIndex("ZDRYLBBJ")));
			escaped.setZdryxl(cursor.getString(cursor.getColumnIndex("ZDRYXL")));
			escaped.setLadw(cursor.getString(cursor.getColumnIndex("LADW")));
			escaped.setNrbjzdryksj(formatter.parse(cursor.getString(cursor
					.getColumnIndex("NRBJZDRYKSJ"))));
			escaped.setHjdqh(cursor.getString(cursor.getColumnIndex("HJDQH")));
			escaped.setHjdxz(cursor.getString(cursor.getColumnIndex("HJDXZ")));
			escaped.setXzdqh(cursor.getString(cursor.getColumnIndex("XZDQH")));
			escaped.setXzdxz(cursor.getString(cursor.getColumnIndex("XZDXZ")));
			escaped.setZjlasj(formatter.parse(cursor.getString(cursor
					.getColumnIndex("ZJLASJ"))));
			escapedList.add(escaped);
		}
		return escapedList;
	}

	public Escaped queryLocalLatestEscaped() throws ParseException {
		SQLiteDatabase db = helper.getReadableDatabase();
		Escaped escaped = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select * from " + MyHelper.TABLE_NAME_Escaped
				+ " order by nrbjzdryksj desc limit 1";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			escaped = new Escaped();
			escaped.setXm(cursor.getString(cursor.getColumnIndex("XM")));
			escaped.setXb(cursor.getString(cursor.getColumnIndex("XB")));
			escaped.setSfzh(cursor.getString(cursor.getColumnIndex("SFZH")));
			escaped.setZdrylbbj(cursor.getString(cursor
					.getColumnIndex("ZDRYLBBJ")));
			escaped.setZdryxl(cursor.getString(cursor.getColumnIndex("ZDRYXL")));
			escaped.setLadw(cursor.getString(cursor.getColumnIndex("LADW")));
			escaped.setNrbjzdryksj(formatter.parse(cursor.getString(cursor
					.getColumnIndex("NRBJZDRYKSJ"))));
			escaped.setHjdqh(cursor.getString(cursor.getColumnIndex("HJDQH")));
			escaped.setHjdxz(cursor.getString(cursor.getColumnIndex("HJDXZ")));
			escaped.setXzdqh(cursor.getString(cursor.getColumnIndex("XZDQH")));
			escaped.setXzdxz(cursor.getString(cursor.getColumnIndex("XZDXZ")));
			escaped.setZjlasj(formatter.parse(cursor.getString(cursor
					.getColumnIndex("ZJLASJ"))));
		}
		return escaped;
	}

	/**
	 * 查询身份证核查记录
	 * 
	 * @return
	 */
	public ArrayList<Normal> queryAll_Normal() {
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<Normal> list = new ArrayList<Normal>();
		String sql = "select * from " + MyHelper.TABLE_NAME_Normal;
		Cursor cursor = db.rawQuery(sql, null);
		Normal normal = null;
		while (cursor.moveToNext()) {
			normal = new Normal();
			normal.setId(cursor.getInt(cursor.getColumnIndex("id")));
			normal.setPersonid(cursor.getString(cursor
					.getColumnIndex("persionId")));
			normal.setPersonname(cursor.getString(cursor
					.getColumnIndex("personName")));
			normal.setAddresscode(cursor.getString(cursor
					.getColumnIndex("addressCode")));
			normal.setAddressname(cursor.getString(cursor
					.getColumnIndex("addressName")));
			normal.setAddressgps(cursor.getString(cursor
					.getColumnIndex("addressGPS")));
			normal.setCommittime(cursor.getString(cursor
					.getColumnIndex("commitTime")));
			normal.setUserid(cursor.getInt(cursor.getColumnIndex("userId")));
			normal.setInfosubmit(cursor.getInt(cursor
					.getColumnIndex("infoSubmit")));
			normal.setPersonfp(cursor.getInt(cursor.getColumnIndex("personFp")));
			normal.setComparfp(cursor.getInt(cursor.getColumnIndex("comparFp")));
			normal.setIsescaped(cursor.getInt(cursor
					.getColumnIndex("isEscaped")));
			list.add(normal);
		}
		return list;
	}

	/**
	 * 根据身份证号查询身份证核查记录
	 * 
	 * @return
	 */
	public ArrayList<Normal> queryNormalByPersonid(String personId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<Normal> list = new ArrayList<Normal>();
		String sql = "select * from " + MyHelper.TABLE_NAME_Normal
				+ " where persionId = '" + personId + "'";
		Cursor cursor = db.rawQuery(sql, null);
		Normal normal = null;
		while (cursor.moveToNext()) {
			normal = new Normal();
			normal.setId(cursor.getInt(cursor.getColumnIndex("id")));
			normal.setPersonid(cursor.getString(cursor
					.getColumnIndex("persionId")));
			normal.setPersonname(cursor.getString(cursor
					.getColumnIndex("personName")));
			normal.setAddresscode(cursor.getString(cursor
					.getColumnIndex("addressCode")));
			normal.setAddressname(cursor.getString(cursor
					.getColumnIndex("addressName")));
			normal.setAddressgps(cursor.getString(cursor
					.getColumnIndex("addressGPS")));
			normal.setCommittime(cursor.getString(cursor
					.getColumnIndex("commitTime")));
			normal.setUserid(cursor.getInt(cursor.getColumnIndex("userId")));
			normal.setInfosubmit(cursor.getInt(cursor
					.getColumnIndex("infoSubmit")));
			normal.setPersonfp(cursor.getInt(cursor.getColumnIndex("personFp")));
			normal.setComparfp(cursor.getInt(cursor.getColumnIndex("comparFp")));
			normal.setIsescaped(cursor.getInt(cursor
					.getColumnIndex("isEscaped")));
			list.add(normal);
		}
		return list;
	}

	/**
	 * 查询未提交身份证核查记录
	 * 
	 * @return
	 */
	public ArrayList<Normal> queryAll_Normal_NotSubmit() {
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<Normal> list = new ArrayList<Normal>();
		String sql = "select * from " + MyHelper.TABLE_NAME_Normal
				+ " where infoSubmit = 0";
		Cursor cursor = db.rawQuery(sql, null);
		Normal normal = null;
		while (cursor.moveToNext()) {
			normal = new Normal();
			normal.setId(cursor.getInt(cursor.getColumnIndex("id")));
			normal.setPersonid(cursor.getString(cursor
					.getColumnIndex("persionId")));
			normal.setPersonname(cursor.getString(cursor
					.getColumnIndex("personName")));
			normal.setAddresscode(cursor.getString(cursor
					.getColumnIndex("addressCode")));
			normal.setAddressname(cursor.getString(cursor
					.getColumnIndex("addressName")));
			normal.setAddressgps(cursor.getString(cursor
					.getColumnIndex("addressGPS")));
			normal.setCommittime(cursor.getString(cursor
					.getColumnIndex("commitTime")));
			normal.setUserid(cursor.getInt(cursor.getColumnIndex("userId")));
			normal.setInfosubmit(cursor.getInt(cursor
					.getColumnIndex("infoSubmit")));
			normal.setPersonfp(cursor.getInt(cursor.getColumnIndex("personFp")));
			normal.setComparfp(cursor.getInt(cursor.getColumnIndex("comparFp")));
			normal.setIsescaped(cursor.getInt(cursor
					.getColumnIndex("isEscaped")));
			list.add(normal);
		}
		return list;
	}

	/**
	 * 按姓名进行查找并排序（符合条件的所有人员信息）
	 */
	public List<Person> queryByname_PoliceInformation(String username) {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Person> list = new ArrayList<Person>();
		Cursor cursor = db.query(MyHelper.TABLE_NAME_PoliceInformation,
				new String[] { "id", "username", "password" },
				"username like ? ", new String[] { "%" + username + "%" },
				null, null, "username asc");
		while (cursor.moveToNext()) {
			Person person = new Person();
			person.setId(cursor.getInt(cursor.getColumnIndex("id")));
			person.setUsername(cursor.getString(cursor
					.getColumnIndex("username")));
			person.setPassword(cursor.getString(cursor
					.getColumnIndex("password")));
			list.add(person);
		}
		db.close();
		return list;
	}

	/**
	 * 根据账号查找
	 */
	public Person queryByUsername_PoliceInformation(String username) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "SELECT * FROM " + MyHelper.TABLE_NAME_PoliceInformation
				+ " WHERE username = '" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		Person person = new Person();
		if (cursor.moveToNext()) {
			person.setUsername(cursor.getString(cursor
					.getColumnIndex("username")));
			person.setPassword(cursor.getString(cursor
					.getColumnIndex("password")));
			db.close();
			return person;
		} else {
			db.close();
			return null;
		}

	}

	/**
	 * 按身份证号查找指纹特征信息
	 */
	public FingerPrintTemplate queryByIDNo_FingerPrintTemplate(String IDNo) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "SELECT * FROM " + MyHelper.TABLE_NAME_FingerPrintTemplate
				+ " WHERE mIDNo='" + IDNo + "';";
		Cursor cursor = db.rawQuery(sql, null);
		FingerPrintTemplate fingerPrintTemplate = new FingerPrintTemplate();
		if (cursor.moveToNext()) {
			fingerPrintTemplate.setmIDNo(cursor.getString(cursor
					.getColumnIndex("mIDNo")));
			fingerPrintTemplate.setRight_Thumb_Template(cursor.getString(cursor
					.getColumnIndex("Right_Thumb_Template")));
			fingerPrintTemplate.setRight_ForeFinger_Template(cursor
					.getString(cursor
							.getColumnIndex("Right_ForeFinger_Template")));
			fingerPrintTemplate.setRight_MiddleFinger_Template(cursor
					.getString(cursor
							.getColumnIndex("Right_MiddleFinger_Template")));
			fingerPrintTemplate.setRight_RingFinger_Template(cursor
					.getString(cursor
							.getColumnIndex("Right_RingFinger_Template")));
			fingerPrintTemplate.setRight_LittleFinger_Template(cursor
					.getString(cursor
							.getColumnIndex("Right_LittleFinger_Template")));
			fingerPrintTemplate.setLeft_Thumb_Template(cursor.getString(cursor
					.getColumnIndex("Left_Thumb_Template")));
			fingerPrintTemplate.setLeft_ForeFinger_Template(cursor
					.getString(cursor
							.getColumnIndex("Left_ForeFinger_Template")));
			fingerPrintTemplate.setLeft_MiddleFinger_Template(cursor
					.getString(cursor
							.getColumnIndex("Left_MiddleFinger_Template")));
			fingerPrintTemplate.setLeft_RingFinger_Template(cursor
					.getString(cursor
							.getColumnIndex("Left_RingFinger_Template")));
			fingerPrintTemplate.setLeft_LittleFinger_Template(cursor
					.getString(cursor
							.getColumnIndex("Left_LittleFinger_Template")));
			db.close();
			return fingerPrintTemplate;
		} else {
			db.close();
			return null;
		}
	}

	/**
	 * 根据查询语句来进行相关查询
	 */
	public Person queryBySQL_PoliceInformation(String sql) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		Person person = new Person();
		if (cursor.moveToNext()) {
			person.setUsername(cursor.getString(cursor
					.getColumnIndex("username")));
		}
		db.close();
		return person;
	}

	public int queryBySQL_FingerPrintTemplat(String sql) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		int num = 0;
		if (cursor.moveToNext()) {
			num = cursor.getCount();
		}
		db.close();
		return num;
	}

	/* 查询记录数 */
	public int queryNumBySQL(String sql) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		int num = 0;
		if (cursor.moveToNext()) {
			num = cursor.getCount();
		}
		db.close();
		return num;
	}

	// public int queryBySQL_FingerPrintTemplatS(String sql){
	// SQLiteDatabase db = helper.getReadableDatabase();
	// Cursor cursor = db.rawQuery(sql, null);
	// int num = 0;
	// if (cursor.moveToNext()) {
	// num = cursor.getInt(cursor.getColumnIndex("sum"));
	// }
	// db.close();
	// return num;
	// }

}
