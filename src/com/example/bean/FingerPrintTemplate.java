package com.example.bean;

import com.IDWORLD.LAPI;

public class FingerPrintTemplate {
	private int id;
	private String mIDNo;//身份证号

//	private byte Right_Thumb_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//右手大拇指指纹特征
//	private byte Right_ForeFinger_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//右手食指指纹特征
//	private byte Right_MiddleFinger_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//右手中指指纹特征
//	private byte Right_RingFinger_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//右手无名指指纹特征
//	private byte Right_LittleFinger_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//右手小拇指指纹特征
//	
//	private byte Left_Thumb_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//左手大拇指指纹特征
//	private byte Left_ForeFinger_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//左手食指指纹特征
//	private byte Left_MiddleFinger_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//左手中指指纹特征
//	private byte Left_RingFinger_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//左手无名指指纹特征
//	private byte Left_LittleFinger_Template[] = new byte[LAPI.WIDTH * LAPI.HEIGHT];//左手小拇指指纹特征
	private String Right_Thumb_Template;//右手大拇指指纹特征
	private String Right_ForeFinger_Template;//右手食指指纹特征
	private String Right_MiddleFinger_Template;//右手中指指纹特征
	private String Right_RingFinger_Template;//右手无名指指纹特征
	private String Right_LittleFinger_Template;//右手小拇指指纹特征
	
	private String Left_Thumb_Template;//左手大拇指指纹特征
	private String Left_ForeFinger_Template;//左手食指指纹特征
	private String Left_MiddleFinger_Template;//左手中指指纹特征
	private String Left_RingFinger_Template;//左手无名指指纹特征
	private String Left_LittleFinger_Template;//左手小拇指指纹特征
	public FingerPrintTemplate(){
		super();
	}
	public FingerPrintTemplate(String mIDNo, String right_Thumb_Template,
			String right_ForeFinger_Template,
			String right_MiddleFinger_Template,
			String right_RingFinger_Template,
			String right_LittleFinger_Template, String left_Thumb_Template,
			String left_ForeFinger_Template, String left_MiddleFinger_Template,
			String left_RingFinger_Template, String left_LittleFinger_Template) {
		super();
		this.mIDNo = mIDNo;
		Right_Thumb_Template = right_Thumb_Template;
		Right_ForeFinger_Template = right_ForeFinger_Template;
		Right_MiddleFinger_Template = right_MiddleFinger_Template;
		Right_RingFinger_Template = right_RingFinger_Template;
		Right_LittleFinger_Template = right_LittleFinger_Template;
		Left_Thumb_Template = left_Thumb_Template;
		Left_ForeFinger_Template = left_ForeFinger_Template;
		Left_MiddleFinger_Template = left_MiddleFinger_Template;
		Left_RingFinger_Template = left_RingFinger_Template;
		Left_LittleFinger_Template = left_LittleFinger_Template;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getmIDNo() {
		return mIDNo;
	}
	public void setmIDNo(String mIDNo) {
		this.mIDNo = mIDNo;
	}
	public String getRight_Thumb_Template() {
		return Right_Thumb_Template;
	}
	public void setRight_Thumb_Template(String right_Thumb_Template) {
		Right_Thumb_Template = right_Thumb_Template;
	}
	public String getRight_ForeFinger_Template() {
		return Right_ForeFinger_Template;
	}
	public void setRight_ForeFinger_Template(String right_ForeFinger_Template) {
		Right_ForeFinger_Template = right_ForeFinger_Template;
	}
	public String getRight_MiddleFinger_Template() {
		return Right_MiddleFinger_Template;
	}
	public void setRight_MiddleFinger_Template(String right_MiddleFinger_Template) {
		Right_MiddleFinger_Template = right_MiddleFinger_Template;
	}
	public String getRight_RingFinger_Template() {
		return Right_RingFinger_Template;
	}
	public void setRight_RingFinger_Template(String right_RingFinger_Template) {
		Right_RingFinger_Template = right_RingFinger_Template;
	}
	public String getRight_LittleFinger_Template() {
		return Right_LittleFinger_Template;
	}
	public void setRight_LittleFinger_Template(String right_LittleFinger_Template) {
		Right_LittleFinger_Template = right_LittleFinger_Template;
	}
	public String getLeft_Thumb_Template() {
		return Left_Thumb_Template;
	}
	public void setLeft_Thumb_Template(String left_Thumb_Template) {
		Left_Thumb_Template = left_Thumb_Template;
	}
	public String getLeft_ForeFinger_Template() {
		return Left_ForeFinger_Template;
	}
	public void setLeft_ForeFinger_Template(String left_ForeFinger_Template) {
		Left_ForeFinger_Template = left_ForeFinger_Template;
	}
	public String getLeft_MiddleFinger_Template() {
		return Left_MiddleFinger_Template;
	}
	public void setLeft_MiddleFinger_Template(String left_MiddleFinger_Template) {
		Left_MiddleFinger_Template = left_MiddleFinger_Template;
	}
	public String getLeft_RingFinger_Template() {
		return Left_RingFinger_Template;
	}
	public void setLeft_RingFinger_Template(String left_RingFinger_Template) {
		Left_RingFinger_Template = left_RingFinger_Template;
	}
	public String getLeft_LittleFinger_Template() {
		return Left_LittleFinger_Template;
	}
	public void setLeft_LittleFinger_Template(String left_LittleFinger_Template) {
		Left_LittleFinger_Template = left_LittleFinger_Template;
	}

//	public FingerPrintTemplate(String mIDNo, byte[] right_Thumb_Template,
//			byte[] right_ForeFinger_Template,
//			byte[] right_MiddleFinger_Template,
//			byte[] right_RingFinger_Template,
//			byte[] right_LittleFinger_Template, byte[] left_Thumb_Template,
//			byte[] left_ForeFinger_Template, byte[] left_MiddleFinger_Template,
//			byte[] left_RingFinger_Template, byte[] left_LittleFinger_Template) {
//		super();
//		this.mIDNo = mIDNo;
//		Right_Thumb_Template = right_Thumb_Template;
//		Right_ForeFinger_Template = right_ForeFinger_Template;
//		Right_MiddleFinger_Template = right_MiddleFinger_Template;
//		Right_RingFinger_Template = right_RingFinger_Template;
//		Right_LittleFinger_Template = right_LittleFinger_Template;
//		Left_Thumb_Template = left_Thumb_Template;
//		Left_ForeFinger_Template = left_ForeFinger_Template;
//		Left_MiddleFinger_Template = left_MiddleFinger_Template;
//		Left_RingFinger_Template = left_RingFinger_Template;
//		Left_LittleFinger_Template = left_LittleFinger_Template;
//	}
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getmIDNo() {
//		return mIDNo;
//	}
//
//	public void setmIDNo(String mIDNo) {
//		this.mIDNo = mIDNo;
//	}
//
//	public byte[] getRight_Thumb_Template() {
//		return Right_Thumb_Template;
//	}
//
//	public void setRight_Thumb_Template(byte[] right_Thumb_Template) {
//		Right_Thumb_Template = right_Thumb_Template;
//	}
//
//	public byte[] getRight_ForeFinger_Template() {
//		return Right_ForeFinger_Template;
//	}
//
//	public void setRight_ForeFinger_Template(byte[] right_ForeFinger_Template) {
//		Right_ForeFinger_Template = right_ForeFinger_Template;
//	}
//
//	public byte[] getRight_MiddleFinger_Template() {
//		return Right_MiddleFinger_Template;
//	}
//
//	public void setRight_MiddleFinger_Template(byte[] right_MiddleFinger_Template) {
//		Right_MiddleFinger_Template = right_MiddleFinger_Template;
//	}
//
//	public byte[] getRight_RingFinger_Template() {
//		return Right_RingFinger_Template;
//	}
//
//	public void setRight_RingFinger_Template(byte[] right_RingFinger_Template) {
//		Right_RingFinger_Template = right_RingFinger_Template;
//	}
//
//	public byte[] getRight_LittleFinger_Template() {
//		return Right_LittleFinger_Template;
//	}
//
//	public void setRight_LittleFinger_Template(byte[] right_LittleFinger_Template) {
//		Right_LittleFinger_Template = right_LittleFinger_Template;
//	}
//
//	public byte[] getLeft_Thumb_Template() {
//		return Left_Thumb_Template;
//	}
//
//	public void setLeft_Thumb_Template(byte[] left_Thumb_Template) {
//		Left_Thumb_Template = left_Thumb_Template;
//	}
//
//	public byte[] getLeft_ForeFinger_Template() {
//		return Left_ForeFinger_Template;
//	}
//
//	public void setLeft_ForeFinger_Template(byte[] left_ForeFinger_Template) {
//		Left_ForeFinger_Template = left_ForeFinger_Template;
//	}
//
//	public byte[] getLeft_MiddleFinger_Template() {
//		return Left_MiddleFinger_Template;
//	}
//
//	public void setLeft_MiddleFinger_Template(byte[] left_MiddleFinger_Template) {
//		Left_MiddleFinger_Template = left_MiddleFinger_Template;
//	}
//
//	public byte[] getLeft_RingFinger_Template() {
//		return Left_RingFinger_Template;
//	}
//
//	public void setLeft_RingFinger_Template(byte[] left_RingFinger_Template) {
//		Left_RingFinger_Template = left_RingFinger_Template;
//	}
//
//	public byte[] getLeft_LittleFinger_Template() {
//		return Left_LittleFinger_Template;
//	}
//
//	public void setLeft_LittleFinger_Template(byte[] left_LittleFinger_Template) {
//		Left_LittleFinger_Template = left_LittleFinger_Template;
//	} 
	
	

}
