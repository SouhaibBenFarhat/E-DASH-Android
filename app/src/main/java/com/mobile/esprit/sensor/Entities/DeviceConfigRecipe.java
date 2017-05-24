package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Souhaib on 30/04/2017.
 */

public class DeviceConfigRecipe implements Parcelable {

    private int id;
    private String description;
    private String date;
    private String imageUrl;
    private String name;
    private int steep;
    private float baseQuantity;
    private float volume;
    private int likes;
    private int comments;
    private int votes;
    private float totalAromeQuantity;
    private Base base;
    private User user;
    private DeviceConfig deviceConfig;
    private ArrayList<AromePerRecipe> aromes;


    protected DeviceConfigRecipe(Parcel in) {
        id = in.readInt();
        description = in.readString();
        date = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        steep = in.readInt();
        baseQuantity = in.readFloat();
        volume = in.readFloat();
        likes = in.readInt();
        comments = in.readInt();
        votes = in.readInt();
        totalAromeQuantity = in.readFloat();
        base = in.readParcelable(Base.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        deviceConfig = in.readParcelable(DeviceConfig.class.getClassLoader());
        aromes = in.createTypedArrayList(AromePerRecipe.CREATOR);
    }

    public static final Creator<DeviceConfigRecipe> CREATOR = new Creator<DeviceConfigRecipe>() {
        @Override
        public DeviceConfigRecipe createFromParcel(Parcel in) {
            return new DeviceConfigRecipe(in);
        }

        @Override
        public DeviceConfigRecipe[] newArray(int size) {
            return new DeviceConfigRecipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSteep() {
        return steep;
    }

    public void setSteep(int steep) {
        this.steep = steep;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public float getTotalAromeQuantity() {
        return totalAromeQuantity;
    }

    public void setTotalAromeQuantity(float totalAromeQuantity) {
        this.totalAromeQuantity = totalAromeQuantity;
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

    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    public void setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public ArrayList<AromePerRecipe> getAromes() {
        return aromes;
    }

    public void setAromes(ArrayList<AromePerRecipe> aromes) {
        this.aromes = aromes;
    }

    public DeviceConfigRecipe() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(imageUrl);
        parcel.writeString(name);
        parcel.writeInt(steep);
        parcel.writeFloat(baseQuantity);
        parcel.writeFloat(volume);
        parcel.writeInt(likes);
        parcel.writeInt(comments);
        parcel.writeInt(votes);
        parcel.writeFloat(totalAromeQuantity);
        parcel.writeParcelable(base, i);
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(deviceConfig, i);
        parcel.writeTypedList(aromes);
    }
}
