package com.example.springboot_401.Security;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
    User findByEmail(String email);
    User findByUsername(String username);
    Long countByEmail(String email);
    Long countByUsername(String username);
}
