package com.example.springboot_401;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role("AUTHOR"));

        Role authorRole = roleRepository.findByRole("AUTHOR");

        User user = new User("jim@jim.com", "password", "Jim", "Jimmerson", true, "jim");
        user.setRoles(Arrays.asList(authorRole));
        user.setProfileImg("https://res.cloudinary.com/dn5oij7hb/image/upload/v1557710793/avatar-man-glasses-512.png");
        userRepository.save(user);

        user = new User("admin@admin.com", "password", "Admin", "User", true, "admin");
        user.setRoles(Arrays.asList(authorRole));
        user.setProfileImg("https://res.cloudinary.com/dn5oij7hb/image/upload/v1557710793/avatar-man-glasses-512.png");
        userRepository.save(user);
    }
}