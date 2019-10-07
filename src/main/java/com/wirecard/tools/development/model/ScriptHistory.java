package com.wirecard.tools.development.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class ScriptHistory {
    @Id
    private String _id;

    private String module;
    private String actionType;
    private String valueId;
    private String script;
    private Date logDate;
    private String userId;
    private String databaseType;
    private String structure;

    public ScriptHistory() {}

    public ScriptHistory(String _id, String module, String actionType, String valueId, String script, Date logDate, String userId, String databaseType, String structure) {
        this._id = _id;
        this.module = module;
        this.actionType = actionType;
        this.valueId = valueId;
        this.script = script;
        this.logDate = logDate;
        this.userId = userId;
        this.databaseType = databaseType;
        this.structure = structure;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }
}
