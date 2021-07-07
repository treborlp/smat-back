package com.inaigem.smatbackend.data;

import javax.persistence.*;

@Entity
@Table
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String dataName;
    private String urlData;

    public Data() {
    }

    public Data(String dataName, String urlData) {
        this.dataName = dataName;
        this.urlData = urlData;
    }

    public Data(int id, String dataName, String urlData) {
        this.id = id;
        this.dataName = dataName;
        this.urlData = urlData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getUrlData() {
        return urlData;
    }

    public void setUrlData(String urlData) {
        this.urlData = urlData;
    }
}
