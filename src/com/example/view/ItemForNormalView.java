package com.example.view;

import android.graphics.Bitmap;

public class ItemForNormalView {
	
	private Bitmap uploadIcon;
	
	private Bitmap fingerprintIcon;

	private String personId;

	private String personName;
	
	private String addressName;
	
	private String commitTime;
	
	private String userName;
	
	private Bitmap More;
	public Bitmap getMore() {
		return More;
	}
	public void setMore(Bitmap more) {
		More = more;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Bitmap getUploadIcon() {
		return uploadIcon;
	}

	public void setUploadIcon(Bitmap uploadIcon) {
		this.uploadIcon = uploadIcon;
	}

	public Bitmap getFingerprintIcon() {
		return fingerprintIcon;
	}

	public void setFingerprintIcon(Bitmap fingerprintIcon) {
		this.fingerprintIcon = fingerprintIcon;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}

	
	
	
}
