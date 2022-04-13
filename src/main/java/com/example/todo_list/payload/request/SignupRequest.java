package com.example.todo_list.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String username;
    @NotBlank
    private String password;
    private Set<String> role;
}
