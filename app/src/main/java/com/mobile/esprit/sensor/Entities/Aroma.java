package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gadour on 12/03/2017.
 */

public class Aroma implements Parcelable {

    private int idAroma;
    private String name;
    private Manufacturer manufacturer;
    private String description;
    private String imgArome;
    private Category category;
    private String date;
    private User user;
    private boolean enabled;


    protected Aroma(Parcel in) {
        idAroma = in.readInt();
        name = in.readString();
        manufacturer = in.readParcelable(Manufacturer.class.getClassLoader());
        description = in.readString();
        imgArome = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        date = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        enabled = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idAroma);
        dest.writeString(name);
        dest.writeParcelable(manufacturer, flags);
        dest.writeString(description);
        dest.writeString(imgArome);
        dest.writeParcelable(category, flags);
        dest.writeString(date);
        dest.writeParcelable(user, flags);
        dest.writeByte((byte) (enabled ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Aroma> CREATOR = new Creator<Aroma>() {
        @Override
        public Aroma createFromParcel(Parcel in) {
            return new Aroma(in);
        }

        @Override
        public Aroma[] newArray(int size) {
            return new Aroma[size];
        }
    };

    public int getIdAroma() {
        return idAroma;
    }

    public void setIdAroma(int idAroma) {
        this.idAroma = idAroma;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgArome() {
        return imgArome;
    }

    public void setImgArome(String imgArome) {
        this.imgArome = imgArome;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Aroma() {
    }

    @Override
    public String toString() {
        return "Aroma{" +
                "idAroma=" + idAroma +
                ", name='" + name + '\'' +
                ", manufacturer=" + manufacturer +
                ", description='" + description + '\'' +
                ", imgArome='" + imgArome + '\'' +
                ", category=" + category +
                ", date='" + date + '\'' +
                '}';
    }

    public Aroma(String name, Manufacturer manufacturer, String description, String imgArome, Category category, String date) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.imgArome = imgArome;
        this.category = category;
        this.date = date;
    }

    public Aroma(int idAroma, String name, Manufacturer manufacturer, String description, String imgArome, Category category, String date) {
        this.idAroma = idAroma;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.imgArome = imgArome;
        this.category = category;
        this.date = date;
    }


    @Override
    public int hashCode() {
        return this.getIdAroma();
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj != null && obj instanceof Aroma) {
            isEqual = (this.idAroma == ((Aroma) obj).getIdAroma());
        }

        return isEqual;
    }



}
