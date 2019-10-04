package com.wirecard.tools.development.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class HistoryLog {
    @Id
    public String _id;

    public String activityType;
    public String description;
    public String action;
    public String oldValue;
    public String newValue;
    public Date modifiedDate;
    public String modifiedBy;
    public String comment;
    public String releaseId;
    public String developmentId;

    public HistoryLog() {}

    public HistoryLog(String _id, String activityType, String description, String action, String oldValue, String newValue, Date modifiedDate, String modifiedBy, String comment, String releaseId, String developmentId) {
        this._id = _id;
        this.activityType = activityType;
        this.description = description;
        this.action = action;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
        this.comment = comment;
        this.releaseId = releaseId;
        this.developmentId = developmentId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public String getDevelopmentId() {
        return developmentId;
    }

    public void setDevelopmentId(String developmentId) {
        this.developmentId = developmentId;
    }
}
