package com.ethercamp.starter.contracts.EthNotary;

/**
 * Created by jacob on 2/23/17.
 */
public class GetDocument {
    String title;
    String cr;
    String document;
    String sender;
    Integer time;
    Integer createdNo;
    Integer nowNo;

    public GetDocument(String title) {
        this.title = title;
    }

    public GetDocument(String title, String cr, String document, String sender, Integer time, Integer createdNo, Integer nowNo) {
        this.title = title;
        this.cr = cr;
        this.document = document;
        this.sender = sender;
        this.time = time;
        this.createdNo = createdNo;
        this.nowNo = nowNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getCreatedNo() {
        return createdNo;
    }

    public void setCreatedNo(Integer createdNo) {
        this.createdNo = createdNo;
    }

    public Integer getNowNo() {
        return nowNo;
    }

    public void setNowNo(Integer nowNo) {
        this.nowNo = nowNo;
    }
}
