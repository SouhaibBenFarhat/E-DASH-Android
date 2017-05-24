package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gadour on 25/03/2017.
 */

public class Base implements Parcelable {

    private int idBase;
    private int pg;
    private int vg;
    private int nicotine;
    private String date;
    private String description;
    private String image;
    private String mobileImageUrl;

    protected Base(Parcel in) {
        idBase = in.readInt();
        pg = in.readInt();
        vg = in.readInt();
        nicotine = in.readInt();
        date = in.readString();
        description = in.readString();
        image = in.readString();
        mobileImageUrl = in.readString();
    }

    public Base(int pg, int vg, int nicotine) {
        this.pg = pg;
        this.vg = vg;
        this.nicotine = nicotine;
    }

    public static final Creator<Base> CREATOR = new Creator<Base>() {
        @Override
        public Base createFromParcel(Parcel in) {
            return new Base(in);
        }

        @Override
        public Base[] newArray(int size) {
            return new Base[size];
        }
    };

    public int getIdBase() {
        return idBase;
    }

    public void setIdBase(int idBase) {
        this.idBase = idBase;
    }

    public int getPg() {
        return pg;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }

    public int getVg() {
        return vg;
    }

    public void setVg(int vg) {
        this.vg = vg;
    }

    public int getNicotine() {
        return nicotine;
    }

    public void setNicotine(int nicotine) {
        this.nicotine = nicotine;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getMobileImageUrl() {
        return mobileImageUrl;
    }

    public void setMobileImageUrl(String mobileImageUrl) {
        this.mobileImageUrl = mobileImageUrl;
    }

    public static Creator<Base> getCREATOR() {
        return CREATOR;
    }

    public Base() {
    }

    @Override
    public String toString() {
        return "Base{" +
                "idBase=" + idBase +
                ", pg=" + pg +
                ", vg=" + vg +
                ", nicotine=" + nicotine +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public Base(int pg, int vg, int nicotine, String date, String description, String image) {
        this.pg = pg;
        this.vg = vg;
        this.nicotine = nicotine;
        this.date = date;
        this.description = description;
        this.image = image;
    }

    public Base(int idBase, int pg, int vg, int nicotine, String date, String description, String image) {
        this.idBase = idBase;
        this.pg = pg;
        this.vg = vg;
        this.nicotine = nicotine;
        this.date = date;
        this.description = description;
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idBase);
        parcel.writeInt(pg);
        parcel.writeInt(vg);
        parcel.writeInt(nicotine);
        parcel.writeString(date);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeString(mobileImageUrl);
    }


    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj != null && obj instanceof Base) {
            if(this.getPg() == ((Base) obj).getPg() && this.getVg() == ((Base) obj).getVg() && this.getNicotine() == ((Base) obj).getNicotine()){
                return true;
            }else{
                return false;
            }
        }

        return isEqual;
    }
}
