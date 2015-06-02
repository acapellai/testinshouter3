package com.shouter.entities;

import java.util.Date;
import java.util.Set;

/**
 * Created by Alex on 22/5/15.
 */
public class JGroup {

    private int groupId;
    private String groupName;
    private String groupDescription;
    private String groupImage;
    private Date groupTsCreation;
    private String groupType;
    private boolean groupBanned;
    private boolean groupHidden;
    private int groupStrikes;
    private int groupPuntuation;
    private JUser userAdmin;
    private Set<JTag> tags;
    private int nMessages;
    private int nUsers;


    public JGroup (String groupName, String groupDescription, JUser userAdmin, Date groupTsCreation){
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.userAdmin = userAdmin;
        this.groupTsCreation = groupTsCreation;
    }

    public JGroup(int groupId, String groupName, String groupDescription,
                  String groupImage, Date groupTsCreation, String groupType,
                  boolean groupBanned, boolean groupHidden, int groupStrikes,
                  int groupPuntuation, JUser userAdmin, Set<JTag> tags) {
        super();
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupImage = groupImage;
        this.groupTsCreation = groupTsCreation;
        this.groupType = groupType;
        this.groupBanned = groupBanned;
        this.groupHidden = groupHidden;
        this.groupStrikes = groupStrikes;
        this.groupPuntuation = groupPuntuation;
        this.userAdmin= userAdmin;
        this.tags = tags;
        this.nUsers = nUsers;
        this.nMessages = nMessages;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public Date getGroupTsCreation() {
        return groupTsCreation;
    }

    public void setGroupTsCreation(Date groupTsCreation) {
        this.groupTsCreation = groupTsCreation;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public boolean isGroupBanned() {
        return groupBanned;
    }

    public void setGroupBanned(boolean groupBanned) {
        this.groupBanned = groupBanned;
    }

    public boolean isGroupHidden() {
        return groupHidden;
    }

    public void setGroupHidden(boolean groupHidden) {
        this.groupHidden = groupHidden;
    }

    public int getGroupStrikes() {
        return groupStrikes;
    }

    public void setGroupStrikes(int groupStrikes) {
        this.groupStrikes = groupStrikes;
    }

    public int getGroupPuntuation() {
        return groupPuntuation;
    }

    public void setGroupPuntuation(int groupPuntuation) {
        this.groupPuntuation = groupPuntuation;
    }

    public JUser getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(JUser userAdmin) {
        this.userAdmin = userAdmin;
    }

    public Set<JTag> getTags() {
        return tags;
    }

    public void setTags(Set<JTag> tags) {
        this.tags = tags;
    }

    public int getnMessages() {
        return nMessages;
    }

    public void setnMessages(int nMessages) {
        this.nMessages = nMessages;
    }

    public int getnUsers() {
        return nUsers;
    }

    public void setnUsers(int nUsers) {
        this.nUsers = nUsers;
    }

    @Override
    public String toString() {
        return "JGroup [groupId=" + groupId + ", groupName=" + groupName
                + ", groupDescription=" + groupDescription + ", groupImage="
                + groupImage + ", groupTsCreation=" + groupTsCreation
                + ", groupType=" + groupType + ", groupBanned=" + groupBanned
                + ", groupHidden=" + groupHidden + ", groupStrikes="
                + groupStrikes + ", groupPuntuation=" + groupPuntuation
                + ", userAdmin=" + userAdmin + ", tags=" + tags + "]";
    }






}
