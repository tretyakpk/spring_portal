package web.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "content_service_providers")
public class CSP {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "csp_id")
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "csp")
    private Set<Parameters> parameters;

    public CSP() {
    }

    public CSP(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
