package com.example.model;

import java.util.Date;
/**
 * 在逃人员
 * @author 陆如一
 * @time 2017-5-26
 */
public class Escaped {
    private Integer id;

    private String xm;

    private String xb;

    private String sfzh;

    private String zdrylbbj;

    private String zdryxl;

    private String ladw;

    private Date nrbjzdryksj;

    private String hjdqh;

    private String hjdxz;

    private String xzdqh;

    private String xzdxz;

    private Date zjlasj;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm == null ? null : xm.trim();
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb == null ? null : xb.trim();
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh == null ? null : sfzh.trim();
    }

    public String getZdrylbbj() {
        return zdrylbbj;
    }

    public void setZdrylbbj(String zdrylbbj) {
        this.zdrylbbj = zdrylbbj == null ? null : zdrylbbj.trim();
    }

    public String getZdryxl() {
        return zdryxl;
    }

    public void setZdryxl(String zdryxl) {
        this.zdryxl = zdryxl == null ? null : zdryxl.trim();
    }

    public String getLadw() {
        return ladw;
    }

    public void setLadw(String ladw) {
        this.ladw = ladw == null ? null : ladw.trim();
    }

    public Date getNrbjzdryksj() {
        return nrbjzdryksj;
    }

    public void setNrbjzdryksj(Date nrbjzdryksj) {
        this.nrbjzdryksj = nrbjzdryksj;
    }

    public String getHjdqh() {
        return hjdqh;
    }

    public void setHjdqh(String hjdqh) {
        this.hjdqh = hjdqh == null ? null : hjdqh.trim();
    }

    public String getHjdxz() {
        return hjdxz;
    }

    public void setHjdxz(String hjdxz) {
        this.hjdxz = hjdxz == null ? null : hjdxz.trim();
    }

    public String getXzdqh() {
        return xzdqh;
    }

    public void setXzdqh(String xzdqh) {
        this.xzdqh = xzdqh == null ? null : xzdqh.trim();
    }

    public String getXzdxz() {
        return xzdxz;
    }

    public void setXzdxz(String xzdxz) {
        this.xzdxz = xzdxz == null ? null : xzdxz.trim();
    }

    public Date getZjlasj() {
        return zjlasj;
    }

    public void setZjlasj(Date zjlasj) {
        this.zjlasj = zjlasj;
    }
}