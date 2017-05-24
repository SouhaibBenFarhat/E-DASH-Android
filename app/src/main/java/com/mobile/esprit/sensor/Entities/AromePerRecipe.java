package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Souhaib on 24/03/2017.
 */

public class AromePerRecipe implements Parcelable {

    private int id;
    private float quantity;
    private Aroma arome;
    private int position;

    public AromePerRecipe() {
    }

    public AromePerRecipe(float quantity, Aroma arome, int position) {
        this.quantity = quantity;
        this.arome = arome;
        this.position = position;
    }

    public AromePerRecipe(int id, float quantity, Aroma arome, int position) {
        this.id = id;
        this.quantity = quantity;
        this.arome = arome;
        this.position = position;
    }

    protected AromePerRecipe(Parcel in) {
        id = in.readInt();
        quantity = in.readFloat();
        arome = in.readParcelable(Aroma.class.getClassLoader());
        position = in.readInt();
    }

    public static final Creator<AromePerRecipe> CREATOR = new Creator<AromePerRecipe>() {
        @Override
        public AromePerRecipe createFromParcel(Parcel in) {
            return new AromePerRecipe(in);
        }

        @Override
        public AromePerRecipe[] newArray(int size) {
            return new AromePerRecipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Aroma getArome() {
        return arome;
    }

    public void setArome(Aroma arome) {
        this.arome = arome;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "AromePerRecipe{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", arome=" + arome +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeFloat(quantity);
        parcel.writeParcelable(arome, i);
        parcel.writeInt(position);
    }


    @Override
    public int hashCode() {
        return this.getArome().getIdAroma();
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj != null && obj instanceof AromePerRecipe) {
            isEqual = (this.getArome().equals(((AromePerRecipe) obj).getArome()));
        }

        return isEqual;
    }
    public AromePerRecipe clone(){
        AromePerRecipe a = new AromePerRecipe();
        a.id = this.id;
        a.quantity = this.quantity;
        a.arome = this.arome;
        a.position = this.position;

        return a;
    }
}
