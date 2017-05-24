package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Souhaib on 03/05/2017.
 */

public class Boitier implements Parcelable{

    private int id;
    private String macAddress;
    private String date;
    private boolean enabled;

    public Boitier(){

    }


    protected Boitier(Parcel in) {
        id = in.readInt();
        macAddress = in.readString();
        date = in.readString();
        enabled = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(macAddress);
        dest.writeString(date);
        dest.writeByte((byte) (enabled ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Boitier> CREATOR = new Creator<Boitier>() {
        @Override
        public Boitier createFromParcel(Parcel in) {
            return new Boitier(in);
        }

        @Override
        public Boitier[] newArray(int size) {
            return new Boitier[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


}
