package web.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "parameters")
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "parameter_id")
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "csp_id", nullable = false)
    private CSP csp;

    public Parameter() {
    }

    public Parameter(String name, String value, CSP csp) {
        this.name = name;
        this.value = value;
        this.csp = csp;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CSP getCsp() {
        return csp;
    }

    public void setCsp(CSP csp) {
        this.csp = csp;
    }
}


