package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Souhaib on 24/03/2017.
 */

public class Recipe implements Parcelable {

    private int id;
    private String description;
    private String date;
    private String imageUrl;
    private String name;
    private int steep;
    private float baseQuantity;
    private float volume;
    private float totalAromeQuantity;
    private int votes;
    private int likes;
    private int comments;
    private Base base;
    private User user;
    private ArrayList<AromePerRecipe> aromes;
    private int recipeTag;


    public Recipe() {
    }

    public Recipe(int id, String description, String date, String imageUrl, String name, int steep, float baseQuantity, float volume, float totalAromeQuantity, Base base, ArrayList<AromePerRecipe> aromes) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.imageUrl = imageUrl;
        this.name = name;
        this.steep = steep;
        this.baseQuantity = baseQuantity;
        this.volume = volume;
        this.totalAromeQuantity = totalAromeQuantity;
        this.base = base;
        this.aromes = aromes;
    }

    public Recipe(int id, String description, String date, String imageUrl, String name, int steep, float baseQuantity, float volume, float totalAromeQuantity, int votes, int likes, int comments, Base base, ArrayList<AromePerRecipe> aromes) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.imageUrl = imageUrl;
        this.name = name;
        this.steep = steep;
        this.baseQuantity = baseQuantity;
        this.volume = volume;
        this.totalAromeQuantity = totalAromeQuantity;
        this.votes = votes;
        this.likes = likes;
        this.comments = comments;
        this.base = base;
        this.aromes = aromes;
    }


    protected Recipe(Parcel in) {
        id = in.readInt();
        description = in.readString();
        date = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        steep = in.readInt();
        baseQuantity = in.readFloat();
        volume = in.readFloat();
        totalAromeQuantity = in.readFloat();
        votes = in.readInt();
        likes = in.readInt();
        comments = in.readInt();
        base = in.readParcelable(Base.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        aromes = in.createTypedArrayList(AromePerRecipe.CREATOR);
        recipeTag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeInt(steep);
        dest.writeFloat(baseQuantity);
        dest.writeFloat(volume);
        dest.writeFloat(totalAromeQuantity);
        dest.writeInt(votes);
        dest.writeInt(likes);
        dest.writeInt(comments);
        dest.writeParcelable(base, flags);
        dest.writeParcelable(user, flags);
        dest.writeTypedList(aromes);
        dest.writeInt(recipeTag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
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

    public ArrayList<AromePerRecipe> getAromes() {
        return aromes;
    }

    public void setAromes(ArrayList<AromePerRecipe> aromes) {
        this.aromes = aromes;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRecipeTag() {
        return recipeTag;
    }

    public void setRecipeTag(int recipeTag) {
        this.recipeTag = recipeTag;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", steep=" + steep +
                ", baseQuantity=" + baseQuantity +
                ", volume=" + volume +
                ", totalAromeQuantity=" + totalAromeQuantity +
                ", base=" + base +
                ", aromes=" + aromes +
                '}';
    }



}
