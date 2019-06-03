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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class BullhornController {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String homePage(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("user", userService.getUser());
        return "list";
    }

    @GetMapping("/add")
    public String addMessage(Model model){
        model.addAttribute("user", userService.getUser());
        model.addAttribute("message", new Message());
        return "messageform";
    }

    @PostMapping("/process")
    public String processMessage(@RequestParam("file") MultipartFile file, @Valid Message message, BindingResult result, Model model){
        model.addAttribute("user", userService.getUser());
        message.setPostedDate(getCurrentTime());
        if(result.hasErrors()){
            return "messageform";
        }
        if(file.isEmpty() && message.getImageUrl().isEmpty()){
            message.setImageUrl("https://res.cloudinary.com/dn5oij7hb/image/upload/v1559574176/Not_available.jpg");
        }
        try{
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            message.setImageUrl(uploadResult.get("url").toString());
            message.setAuthorId(userService.getUser().getId());
            messageRepository.save(message);
        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/";
        }
        model.addAttribute("message", message);
        return "redirect:/";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        model.addAttribute("user", userService.getUser());
        return "messageform";
    }

    @RequestMapping("/view/{id}")
    public String viewMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        model.addAttribute("user", userService.getUser());
        return "show";
    }

    @RequestMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/profile")
    public String getProfile(Model model){
        model.addAttribute("user", userService.getUser());
        model.addAttribute("messages", messageRepository.findAllByAuthorId(userService.getUser().getId()));
        return "profile";
    }

    @RequestMapping("/profile/{id}")
    public String loadProfile(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userService.getUser());
        model.addAttribute("profileUser", userService.userRepository.findById(id).get());
        model.addAttribute("messages", messageRepository.findAllByAuthorId(id));
        return "userprofile";
    }


    public String getCurrentTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
