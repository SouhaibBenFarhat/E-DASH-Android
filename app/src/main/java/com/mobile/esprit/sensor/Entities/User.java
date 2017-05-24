package com.mobile.esprit.sensor.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Souhaib on 04/02/2017.
 */

public class User implements Parcelable {

    private static User CurrentUser;

    private String id;
    private String login;
    private String email;
    private String password;
    private String provider;
    private String profileId;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private String linkUri;
    private Boitier boitier;

    public User() {

    }

    public User(String id, String login, String email, String password, String provider, String profileId, String profilePicture, String firstName, String lastName, String linkUri) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.profileId = profileId;
        this.profilePicture = profilePicture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.linkUri = linkUri;
    }


    protected User(Parcel in) {
        id = in.readString();
        login = in.readString();
        email = in.readString();
        password = in.readString();
        provider = in.readString();
        profileId = in.readString();
        profilePicture = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        linkUri = in.readString();
        boitier = in.readParcelable(Boitier.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLinkUri() {
        return linkUri;
    }

    public void setLinkUri(String linkUri) {
        this.linkUri = linkUri;
    }

    public Boitier getBoitier() {
        return boitier;
    }

    public void setBoitier(Boitier boitier) {
        this.boitier = boitier;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", provider='" + provider + '\'' +
                ", profileId='" + profileId + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", linkUri='" + linkUri + '\'' +
                '}';
    }

    public static void setCurrentUser(User user) {

        if (user != null) {
            CurrentUser = user;
            System.out.println("------------------------------------------");
            System.out.println("Current User : " + user.getId() + " " + user.getEmail());
            System.out.println("------------------------------------------");
        } else {
            CurrentUser = null;
        }

    }

    public static User getInstance() {
        if (CurrentUser != null) {
            return CurrentUser;
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(login);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(provider);
        parcel.writeString(profileId);
        parcel.writeString(profilePicture);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(linkUri);
        parcel.writeParcelable(boitier, i);
    }
}
