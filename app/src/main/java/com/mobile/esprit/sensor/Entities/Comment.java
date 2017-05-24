package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Souhaib on 27/03/2017.
 */

public class Comment implements Parcelable {

    private int id;
    private String date;
    private String content;
    private User user;
    private Recipe recipe;

    public Comment(){

    }

    public Comment(int id, String date, String content, User user, Recipe recipe) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.user = user;
        this.recipe = recipe;
    }


    protected Comment(Parcel in) {
        id = in.readInt();
        date = in.readString();
        content = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        recipe = in.readParcelable(Recipe.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(recipe, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", recipeToAdd=" + recipe +
                '}';
    }


}
