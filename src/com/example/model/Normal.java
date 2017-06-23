package com.example.model;

import java.util.Date;
import java.util.List;

/**
 * 核录记录信息
 * @author 陆如一
 * @time 2017-5-24
 */
public class Normal {
	private Integer id;

	private String personid;

	private String personname;

	private String addresscode;

	private String addressname;

	private String addressgps;

	private String committime;

	private Integer userid;

	private Integer infosubmit;
	
	private Integer personfp;
	
	private Integer comparfp;

	public Integer getComparfp() {
		return comparfp;
	}

	public void setComparfp(Integer comparfp) {
		this.comparfp = comparfp;
	}

	public Integer getPersonfp() {
		return personfp;
	}

	public void setPersonfp(Integer personfp) {
		this.personfp = personfp;
	}

	public Integer getInfosubmit() {
		return infosubmit;
	}

	public void setInfosubmit(Integer infosubmit) {
		this.infosubmit = infosubmit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPersonid() {
		return personid;
	}

	public void setPersonid(String personid) {
		this.personid = personid == null ? null : personid.trim();
	}

	public String getPersonname() {
		return personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname == null ? null : personname.trim();
	}

	public String getAddresscode() {
		return addresscode;
	}

	public void setAddresscode(String addresscode) {
		this.addresscode = addresscode == null ? null : addresscode.trim();
	}

	public String getAddressname() {
		return addressname;
	}

	public void setAddressname(String addressname) {
		this.addressname = addressname == null ? null : addressname.trim();
	}

	public String getAddressgps() {
		return addressgps;
	}

	public void setAddressgps(String addressgps) {
		this.addressgps = addressgps == null ? null : addressgps.trim();
	}

	public String getCommittime() {
		return committime;
	}

	public void setCommittime(String committime) {
		this.committime = committime;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
}