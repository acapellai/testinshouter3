package com.shouter.entities;

import java.util.Date;

/**
 * Created by Alex on 22/5/15.
 */
public class JUser {

    private int userId;
    private String userName;
    private String userImage;
    private Date userTsCreation;
    private String userEmail;
    private boolean userBanned;
    private boolean userHidden;
    private int userStrikes;
    private double userPuntuation;

    public JUser(int userId, String userName, String userImage, String userEmail,
                 Date userTsCreation, boolean userBanned, boolean userHidden,
                 int userStrikes, double userPuntuation) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.userTsCreation = userTsCreation;
        this.userBanned = userBanned;
        this.userHidden = userHidden;
        this.userStrikes = userStrikes;
        this.userPuntuation = userPuntuation;
        this.userEmail = userEmail;
    }


    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserImage() {
        return userImage;
    }
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
    public Date getUserTsCreation() {
        return userTsCreation;
    }
    public void setUserTsCreation(Date userTsCreation) {
        this.userTsCreation = userTsCreation;
    }
    public boolean isUserBanned() {
        return userBanned;
    }
    public void setUserBanned(boolean userBanned) {
        this.userBanned = userBanned;
    }
    public boolean isUserHidden() {
        return userHidden;
    }
    public void setUserHidden(boolean userHidden) {
        this.userHidden = userHidden;
    }
    public int getUserStrikes() {
        return userStrikes;
    }
    public void setUserStrikes(int userStrikes) {
        this.userStrikes = userStrikes;
    }
    public double getUserPuntuation() {
        return userPuntuation;
    }
    public void setUserPuntuation(double userPuntuation) {
        this.userPuntuation = userPuntuation;
    }
    @Override
    public String toString() {
        return "JUser [userId=" + userId + ", userName=" + userName
                + ", userImage=" + userImage + ", userTsCreation="
                + userTsCreation + ", userBanned=" + userBanned
                + ", userHidden=" + userHidden + ", userStrikes=" + userStrikes
                + ", userPuntuation=" + userPuntuation + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (userBanned ? 1231 : 1237);
        result = prime * result + (userHidden ? 1231 : 1237);
        result = prime * result + userId;
        result = prime * result
                + ((userImage == null) ? 0 : userImage.hashCode());
        result = prime * result
                + ((userName == null) ? 0 : userName.hashCode());
        long temp;
        temp = Double.doubleToLongBits(userPuntuation);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + userStrikes;
        result = prime * result
                + ((userTsCreation == null) ? 0 : userTsCreation.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JUser other = (JUser) obj;
        if (userBanned != other.userBanned)
            return false;
        if (userHidden != other.userHidden)
            return false;
        if (userId != other.userId)
            return false;
        if (userImage == null) {
            if (other.userImage != null)
                return false;
        } else if (!userImage.equals(other.userImage))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (Double.doubleToLongBits(userPuntuation) != Double
                .doubleToLongBits(other.userPuntuation))
            return false;
        if (userStrikes != other.userStrikes)
            return false;
        if (userTsCreation == null) {
            if (other.userTsCreation != null)
                return false;
        } else if (!userTsCreation.equals(other.userTsCreation))
            return false;
        return true;
    }




}