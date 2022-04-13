package com.example.todo_list.Controller;


import com.example.todo_list.Repository.RoleRepository;
import com.example.todo_list.Repository.UserRepository;
import com.example.todo_list.Security.jwt.JwtUtils;
import com.example.todo_list.Security.services.UserDetailsImp;
import com.example.todo_list.model.A_User;
import com.example.todo_list.model.ERole;
import com.example.todo_list.model.Role;
import com.example.todo_list.payload.request.LoginRequest;
import com.example.todo_list.payload.request.SignupRequest;
import com.example.todo_list.payload.response.MessageResponse;
import com.example.todo_list.payload.response.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
               Authentication authentication = authenticationManager
                       .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
                log.info("You are Okey here {}",loginRequest.getUsername());
        log.info("You are Okey here {}",loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImp userDetails=(UserDetailsImp) authentication.getPrincipal();
        log.info("your name is {}", userDetails.getUsername());
        log.info("your pass is {}",userDetails.getPassword());
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles=userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        log.info("your name is {}",loginRequest.getUsername());
        log.info("your Id is  {}", loginRequest.getPassword());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),userDetails.getUsername(),roles));


    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(){
        ResponseCookie cookie=jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've Been signed out!"));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        if (userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Mail already Used"));
        }
        log.info("your name is {}",signupRequest.getName());
        A_User user = new A_User(signupRequest.getName(),
                signupRequest.getUsername(),
                encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        log.info("your name is {}",user.getName());
        log.info("your name is {}",user.getName());

      Set<Role> roles = new HashSet<>();

      if (strRoles == null){
            Role userRole = roleRepository.findByName(ERole.ROlE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: user lvl  Role is not found"));

            roles.add(userRole);

        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "manager" :
                        Role managerRole = roleRepository.findByName(ERole.ROLE_Manager)
                                .orElseThrow(() -> new RuntimeException("Error : manager lvl Role is not found"));
                        roles.add(managerRole);
                        break;
                    case "admin":
                        Role admineRole = roleRepository.findByName(ERole.ROLE_Manager)
                                .orElseThrow(() -> new RuntimeException(" Error : admin lvl  Role is not found"));
                        roles.add(admineRole);
                        break;
                    default:
                       Role userRole = roleRepository.findByName(ERole.ROlE_USER)
                                .orElseThrow(() -> new RuntimeException("Error :Role is not found"));
                        roles.add(userRole);

                }
            });
        }
        user.setRoles(roles);
        log.info("your name is {}",user.getName());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registred successfully!"));

    }

}
