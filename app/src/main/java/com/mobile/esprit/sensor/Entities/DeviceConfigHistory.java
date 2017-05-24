package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Gadour on 23/04/2017.
 */

public class DeviceConfigHistory implements Parcelable {

    private int id;
    private String note;
    private String date;
    private String name;
    private float baseQuantity;
    private float volume;
    private float totalAromeQuantity;
    private Boolean visibility;
    private Base base;
    private User user;
    private ArrayList<AromePerRecipe> aromas;
    private Boolean isDefault;

    protected DeviceConfigHistory(Parcel in) {
        id = in.readInt();
        note = in.readString();
        date = in.readString();
        name = in.readString();
        baseQuantity = in.readFloat();
        volume = in.readFloat();
        totalAromeQuantity = in.readFloat();
        base = in.readParcelable(Base.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        aromas = in.createTypedArrayList(AromePerRecipe.CREATOR);
    }

    public static final Creator<DeviceConfigHistory> CREATOR = new Creator<DeviceConfigHistory>() {
        @Override
        public DeviceConfigHistory createFromParcel(Parcel in) {
            return new DeviceConfigHistory(in);
        }

        @Override
        public DeviceConfigHistory[] newArray(int size) {
            return new DeviceConfigHistory[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(float baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getTotalAromeQuantity() {
        return totalAromeQuantity;
    }

    public void setTotalAromeQuantity(float totalAromeQuantity) {
        this.totalAromeQuantity = totalAromeQuantity;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<AromePerRecipe> getAromas() {
        return aromas;
    }

    public void setAromas(ArrayList<AromePerRecipe> aromas) {
        this.aromas = aromas;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public DeviceConfigHistory() {
    }

    public DeviceConfigHistory(int id, String note, String date, String name, float baseQuantity, float volume, float totalAromeQuantity, Boolean visibility, Base base, User user, ArrayList<AromePerRecipe> aromas, Boolean isDefault) {
        this.id = id;
        this.note = note;
        this.date = date;
        this.name = name;
        this.baseQuantity = baseQuantity;
        this.volume = volume;
        this.totalAromeQuantity = totalAromeQuantity;
        this.visibility = visibility;
        this.base = base;
        this.user = user;
        this.aromas = aromas;
        this.isDefault = isDefault;
    }

    public DeviceConfigHistory(String note, String date, String name, float baseQuantity, float volume, float totalAromeQuantity, Boolean visibility, Base base, User user, ArrayList<AromePerRecipe> aromas, Boolean isDefault) {
        this.note = note;
        this.date = date;
        this.name = name;
        this.baseQuantity = baseQuantity;
        this.volume = volume;
        this.totalAromeQuantity = totalAromeQuantity;
        this.visibility = visibility;
        this.base = base;
        this.user = user;
        this.aromas = aromas;
        this.isDefault = isDefault;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(note);
        parcel.writeString(date);
        parcel.writeString(name);
        parcel.writeFloat(baseQuantity);
        parcel.writeFloat(volume);
        parcel.writeFloat(totalAromeQuantity);
        parcel.writeParcelable(base, i);
        parcel.writeParcelable(user, i);
        parcel.writeTypedList(aromas);
    }
}
