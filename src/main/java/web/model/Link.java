package web.model;

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
    private int active;

    @Column(name = "created_at")
    private Date created_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "csp_id", nullable = false)
    private CSP csp;

    public Link() {
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public CSP getCsp() {
        return csp;
    }

    public void setCsp(CSP csp) {
        this.csp = csp;
    }
}
