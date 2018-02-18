package web.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class User {

    @Id
    private String id;
    private String name;
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