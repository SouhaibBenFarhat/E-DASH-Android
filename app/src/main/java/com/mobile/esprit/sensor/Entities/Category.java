package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gadour on 22/03/2017.
 */

public class Category implements Parcelable {

    private int idCategory;
    private String name;
    private String imageName;
    private String image;
    private String description;
    private String date;

    protected Category(Parcel in) {
        idCategory = in.readInt();
        name = in.readString();
        imageName = in.readString();
        image = in.readString();
        description = in.readString();
        date = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Category() {
    }

    @Override
    public String toString() {
        return "Category{" +
                "idCategory=" + idCategory +
                ", name='" + name + '\'' +
                ", imageName='" + imageName + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public Category(String name, String imageName, String image, String description, String date) {
        this.name = name;
        this.imageName = imageName;
        this.image = image;
        this.description = description;
        this.date = date;
    }

    public Category(int idCategory, String name, String imageName, String image, String description, String date) {
        this.idCategory = idCategory;
        this.name = name;
        this.imageName = imageName;
        this.image = image;
        this.description = description;
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idCategory);
        parcel.writeString(name);
        parcel.writeString(imageName);
        parcel.writeString(image);
        parcel.writeString(description);
        parcel.writeString(date);
    }

    @Override
    public int hashCode() {
        return this.getIdCategory();
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj != null && obj instanceof Category) {
            isEqual = (this.idCategory == ((Category) obj).getIdCategory());
        }

        return isEqual;
    }
}
