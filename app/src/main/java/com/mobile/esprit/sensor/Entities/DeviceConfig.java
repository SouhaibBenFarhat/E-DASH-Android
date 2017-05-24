package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Souhaib on 01/04/2017.
 */

public class DeviceConfig implements Parcelable {

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
    private DeviceConfigHistory deviceConfigHistory;
    private int deviceConfigTag;


    protected DeviceConfig(Parcel in) {
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
        deviceConfigHistory = in.readParcelable(DeviceConfigHistory.class.getClassLoader());
        deviceConfigTag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(note);
        dest.writeString(date);
        dest.writeString(name);
        dest.writeFloat(baseQuantity);
        dest.writeFloat(volume);
        dest.writeFloat(totalAromeQuantity);
        dest.writeParcelable(base, flags);
        dest.writeParcelable(user, flags);
        dest.writeTypedList(aromas);
        dest.writeParcelable(deviceConfigHistory, flags);
        dest.writeInt(deviceConfigTag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceConfig> CREATOR = new Creator<DeviceConfig>() {
        @Override
        public DeviceConfig createFromParcel(Parcel in) {
            return new DeviceConfig(in);
        }

        @Override
        public DeviceConfig[] newArray(int size) {
            return new DeviceConfig[size];
        }
    };

    public DeviceConfigHistory getDeviceConfigHistory() {
        return deviceConfigHistory;
    }

    public void setDeviceConfigHistory(DeviceConfigHistory deviceConfigHistory) {
        this.deviceConfigHistory = deviceConfigHistory;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

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

    public int getDeviceConfigTag() {
        return deviceConfigTag;
    }

    public void setDeviceConfigTag(int deviceConfigTag) {
        this.deviceConfigTag = deviceConfigTag;
    }

    public DeviceConfig() {
    }

    public DeviceConfig(int id, String note, String date, String name, float baseQuantity, float volume, float totalAromeQuantity, Boolean visibility, Base base, User user, ArrayList<AromePerRecipe> aromas, Boolean isDefault, DeviceConfigHistory deviceConfigHistory) {
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
        this.deviceConfigHistory = deviceConfigHistory;
    }

    public DeviceConfig(String note, String date, String name, float baseQuantity, float volume, float totalAromeQuantity, Boolean visibility, Base base, User user, ArrayList<AromePerRecipe> aromas, Boolean isDefault, DeviceConfigHistory deviceConfigHistory) {
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
        this.deviceConfigHistory = deviceConfigHistory;
    }


}
