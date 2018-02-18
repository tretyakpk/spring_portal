package web.entity;

import java.util.Date;

public class Link {

    private String url;
    private Boolean status;
    private Date created_at;

    public Link() {}

    public Link(String url, Boolean status, Date created_at) {
        this.url = url;
        this.status = status;
        this.created_at = created_at;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
