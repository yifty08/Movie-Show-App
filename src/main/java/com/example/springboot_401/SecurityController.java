package com.example.springboot_401;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class SecurityController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") User user, BindingResult result,
                                      Model model){
        model.addAttribute("user", user);
        if (result.hasErrors()){
            return "registration";
        }else{
            userService.saveUser(user, "AUTHOR");
            model.addAttribute("message", "User Account Created");
        }
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/secure")
    public String secure(Model model){
        model.addAttribute("myuser", userService.getUser());
        return "secure";
    }
}
