package web.entity;

import org.springframework.data.annotation.Id;

import java.util.List;

public class CSP {

    @Id
    private String id;
    private String name;
    private List<Link> links;

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
}
