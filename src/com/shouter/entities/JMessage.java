package com.shouter.entities;

import java.util.Date;
import java.util.Set;

/**
 * Created by Aitor on 22/5/15.
 */
public class JMessage {
    private int messageId;
    private String messageText;
    private String messageTittle;
    private String messageImage;
    private boolean messageHidden;
    private Date messageTsCreated;
    private JUser messageUser;
    private int messageGroup;
    private Set<JUser> likes;
    private int idParent;
    private int responses;


    public JMessage (String messageText,String messageTittle, JUser messageUser, int idParent, int messageGroup){
        this.messageImage ="android default";
        this.messageHidden = false;
        this.messageTsCreated = null;
        this.messageGroup = messageGroup;
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTittle = messageTittle;
        this.idParent = idParent;
    }


    public JMessage(int messageId, String messageText, String messageTittle,
                    String messageImage, boolean messageHidden, Date messageTsCreated,
                    JUser messageUser, int messageGroup, Set<JUser> likes, int idParent) {
        super();
        this.messageId = messageId;
        this.messageText = messageText;
        this.messageTittle = messageTittle;
        this.messageImage = messageImage;
        this.messageHidden = messageHidden;
        this.messageTsCreated = messageTsCreated;
        this.messageUser = messageUser;
        this.messageGroup = messageGroup;
        this.likes = likes;
        this.idParent = idParent;
    }


    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public int getMessageId() {
        return messageId;
    }
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    public String getMessageTittle() {
        return messageTittle;
    }
    public void setMessageTittle(String messageTittle) {
        this.messageTittle = messageTittle;
    }
    public String getMessageImage() {
        return messageImage;
    }
    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }
    public boolean isMessageHidden() {
        return messageHidden;
    }
    public void setMessageHidden(boolean messageHidden) {
        this.messageHidden = messageHidden;
    }
    public Date getMessageTsCreated() {
        return messageTsCreated;
    }
    public void setMessageTsCreated(Date messageTsCreated) {
        this.messageTsCreated = messageTsCreated;
    }
    public JUser getMessageUser() {
        return messageUser;
    }
    public void setMessageUser(JUser messageUser) {
        this.messageUser = messageUser;
    }
    public int getMessageGroup() {
        return messageGroup;
    }
    public void setMessageGroup(int messageGroup) {
        this.messageGroup = messageGroup;
    }
    public Set<JUser> getLikes() {
        return likes;
    }
    public void setLikes(Set<JUser> likes) {
        this.likes = likes;
    }

    public int getResponses() {
        return responses;
    }

    public void setResponses(int responses) {
        this.responses = responses;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((likes == null) ? 0 : likes.hashCode());
        result = prime * result + messageGroup;
        result = prime * result + (messageHidden ? 1231 : 1237);
        result = prime * result + messageId;
        result = prime * result
                + ((messageImage == null) ? 0 : messageImage.hashCode());
        result = prime * result
                + ((messageText == null) ? 0 : messageText.hashCode());
        result = prime * result
                + ((messageTittle == null) ? 0 : messageTittle.hashCode());
        result = prime
                * result
                + ((messageTsCreated == null) ? 0 : messageTsCreated.hashCode());
        result = prime * result
                + ((messageUser == null) ? 0 : messageUser.hashCode());
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
        JMessage other = (JMessage) obj;
        if (likes == null) {
            if (other.likes != null)
                return false;
        } else if (!likes.equals(other.likes))
            return false;
        if (messageGroup != other.messageGroup)
            return false;
        if (messageHidden != other.messageHidden)
            return false;
        if (messageId != other.messageId)
            return false;
        if (messageImage == null) {
            if (other.messageImage != null)
                return false;
        } else if (!messageImage.equals(other.messageImage))
            return false;
        if (messageText == null) {
            if (other.messageText != null)
                return false;
        } else if (!messageText.equals(other.messageText))
            return false;
        if (messageTittle == null) {
            if (other.messageTittle != null)
                return false;
        } else if (!messageTittle.equals(other.messageTittle))
            return false;
        if (messageTsCreated == null) {
            if (other.messageTsCreated != null)
                return false;
        } else if (!messageTsCreated.equals(other.messageTsCreated))
            return false;
        if (messageUser == null) {
            if (other.messageUser != null)
                return false;
        } else if (!messageUser.equals(other.messageUser))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "JMessage{" +
                "messageId=" + messageId +
                ", messageText='" + messageText + '\'' +
                ", messageTittle='" + messageTittle + '\'' +
                ", messageImage='" + messageImage + '\'' +
                ", messageHidden=" + messageHidden +
                ", messageTsCreated=" + messageTsCreated +
                ", messageUser=" + messageUser +
                ", messageGroup=" + messageGroup +
                ", likes=" + likes +
                ", idParent=" + idParent +
                ", responses=" + responses +
                '}';
    }
}
