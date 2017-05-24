package com.mobile.esprit.sensor.Entities;

/**
 * Created by Souhaib on 06/02/2017.
 */

public class Device {

    private String id;
    private String token;



    public Device() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
