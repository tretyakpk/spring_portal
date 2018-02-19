package web.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@EntityScan
public class User {

    @Id
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    private String role;

    private Date createdAt;

    private List<ContentServiceProvider> contentServiceProviders;

    public User() {}

    public User(String id, String name, String password, String role, Date createdAt, List<ContentServiceProvider> contentServiceProviders) {

        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.contentServiceProviders = contentServiceProviders;
    }

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.contentServiceProviders = user.getContentServiceProviders();
    }


    public String toString() {
        return String.format(
                "User[id=%s, name='%s', password='%s'm role='%s', createdAt='%s']",
                id, name, password, role, createdAt.toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<ContentServiceProvider> getContentServiceProviders() {
        return contentServiceProviders;
    }

    public void setContentServiceProviders(List<ContentServiceProvider> contentServiceProviders) {
        this.contentServiceProviders = contentServiceProviders;
    }



}