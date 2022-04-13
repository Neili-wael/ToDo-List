package com.example.todo_list.payload.response;


import com.example.todo_list.Security.services.UserDetailsImp;
import lombok.*;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Setter

@Getter



@ToString


public class UserInfoResponse {

    @Id
    private Long id;
    private String username;
    private List<String> roles;

    public UserInfoResponse() {
    }

    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return username;
    }

    public void setMail(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
