package web.entity;

import com.mongodb.DBObject;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Log {

    @Id
    private String id;
    private String type;
    private String msisdn;
    private DBObject parameters;
    private Date createdAt;

    public Log() {
    }

    public Log(String id, String type, String msisdn, DBObject parameters, Date createdAt) {
        this.id = id;
        this.type = type;
        this.msisdn = msisdn;
        this.parameters = parameters;
        this.createdAt = createdAt;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public DBObject getParameters() {
        return parameters;
    }

    public void setParameters(DBObject parameters) {
        this.parameters = parameters;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}