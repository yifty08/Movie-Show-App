package com.example.springboot_401.Security;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long>{
    Role findByRole(String role);
}
