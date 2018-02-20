package web.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "url", length = 1023)
    private String url;

    @Column(name = "active")
    private int active = 1;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "csp_id", nullable = false)
    @JsonBackReference
    private CSP csp;

    public Link() {
    }

    public Link(String url) {
        this.url = url;
    }

    public Link(String url, int active, Date created_at, CSP csp) {
        this.url = url;
        this.active = active;
        this.createdAt = created_at;
        this.csp = csp;
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", active=" + active +
                ", created_at=" + createdAt +
                ", csp=" + csp +
                '}';
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date created_at) {
        this.createdAt = created_at;
    }

    public CSP getCsp() {
        return csp;
    }

    public void setCsp(CSP csp) {
        this.csp = csp;
    }
}
