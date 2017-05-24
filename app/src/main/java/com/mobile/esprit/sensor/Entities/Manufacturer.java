package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gadour on 22/03/2017.
 */

public class Manufacturer implements Parcelable {

    private int idManufacturer;
    private String name;
    private String description;
    private String image;
    private String address;
    private String date;
    private float lang;
    private float lat;


    protected Manufacturer(Parcel in) {
        idManufacturer = in.readInt();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        address = in.readString();
        date = in.readString();
        lang = in.readFloat();
        lat = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idManufacturer);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(address);
        dest.writeString(date);
        dest.writeFloat(lang);
        dest.writeFloat(lat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Manufacturer> CREATOR = new Creator<Manufacturer>() {
        @Override
        public Manufacturer createFromParcel(Parcel in) {
            return new Manufacturer(in);
        }

        @Override
        public Manufacturer[] newArray(int size) {
            return new Manufacturer[size];
        }
    };

    public float getLang() {
        return lang;
    }

    public void setLang(float lang) {
        this.lang = lang;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public int getIdManufacturer() {
        return idManufacturer;
    }

    public void setIdManufacturer(int idManufacturer) {
        this.idManufacturer = idManufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Manufacturer() {
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "idManufacturer=" + idManufacturer +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public Manufacturer(String name, String description, String image, String address, String date) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.address = address;
        this.date = date;
    }

    public Manufacturer(int idManufacturer, String name, String description, String image, String address, String date) {
        this.idManufacturer = idManufacturer;
        this.name = name;
        this.description = description;
        this.image = image;
        this.address = address;
        this.date = date;
    }


}
