package web.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class CSP {

    @Id
    private String id;

    @NotEmpty
    @NotNull
    private String name;

    private List<Link> links;

    private Date createdAt;

    public CSP() { }

    public CSP(String name, List<Link> links) {
        this.name = name;
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
