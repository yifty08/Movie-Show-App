package com.example.springboot_401;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class SecurityController {
    @Autowired
    private UserService userService;

    @Autowired
    CloudinaryConfig cloudc;

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam("file") MultipartFile file, @Valid @ModelAttribute("user") User user, BindingResult result,
                                      Model model){
        model.addAttribute("user", user);
        if(file.isEmpty()){
            return "registration";
        }
        if (result.hasErrors()){
            return "registration";
        }else{
            try{
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                user.setProfileImg(uploadResult.get("url").toString());
                userService.saveUser(user, "AUTHOR");
                model.addAttribute("message", "User Account Created");
            }catch (IOException e){
                e.printStackTrace();
                return "redirect:/";
            }
        }
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String proceed(){
        return "redirect:/";
    }
}
