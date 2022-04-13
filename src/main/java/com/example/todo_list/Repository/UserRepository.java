package com.example.todo_list.Repository;

import com.example.todo_list.model.A_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<A_User, Long> {

    Optional <A_User> findByUsername(String Username);
    Boolean existsByUsername(String mail);
}
