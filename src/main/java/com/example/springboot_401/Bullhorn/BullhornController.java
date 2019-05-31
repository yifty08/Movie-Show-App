package com.example.springboot_401.Bullhorn;

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
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String homePage(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String addMessage(Model model){
        model.addAttribute("message", new Message());
        return "messageform";
    }

    @PostMapping("/process")
    public String processMessage(@RequestParam("file") MultipartFile file, @Valid Message message, BindingResult result, Model model){
        message.setPostedDate(getCurrentTime());
        if(result.hasErrors()){
            return "messageform";
        }
        if(file.isEmpty()){
            return "redirect:/add";
        }
        try{
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            message.setImageUrl(uploadResult.get("url").toString());
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
        return "messageform";
    }

    @RequestMapping("/view/{id}")
    public String viewMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }

    public String getCurrentTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
