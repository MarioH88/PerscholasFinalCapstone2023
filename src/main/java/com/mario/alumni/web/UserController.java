package com.mario.alumni.web;

import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.enums.Role;
import com.mario.alumni.app.exception.InvalidRoleChangeException;
import com.mario.alumni.app.model.UserModel;
import com.mario.alumni.app.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.listAllUsers());
        return "/users/index";
    }



    @GetMapping("delete/{userId}")
    @PreAuthorize("hasAuthority('Admin')")
    public String updateForm( @PathVariable("userId") Long userId, Model model,RedirectAttributes redirectAttributes) {
        userService.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("message", "Successfully deleted the User with Id: " + userId);
        return "redirect:/users";
    }

    @GetMapping("createForm")
    @PreAuthorize("hasAuthority('Admin')")
    public String createForm( Model model) {
        model.addAttribute("user", new UserModel());
        return "/users/createForm";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('Admin')")
    public String createUser(@ModelAttribute("user") UserModel userModel, Model model, RedirectAttributes redirectAttribute) {
        try{
            Set<Role> roles = new HashSet<>();
            if(userModel.getAdmin()) {
                roles.add(Role.ADMIN);
            }
            if(userModel.getOrganiser()) {
                roles.add(Role.ORGANISER);
            }
            if(userModel.getAlumni()) {
                roles.add(Role.ALUMNI);
            }
            User user = userService.createUser(userModel.getEmail(), userModel.getPassword(), roles);

            redirectAttribute.addFlashAttribute("message", "Successfully created the User with Id: " + user.getUserId());
        } catch(InvalidRoleChangeException e) {
            redirectAttribute.addFlashAttribute("warning", "Failed to assign a role to created User with email: " + userModel.getEmail());
        } catch(Exception e) {
            redirectAttribute.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("updateForm/{userId}")
    @PreAuthorize("hasAuthority('Admin')")
    public String updateForm( @PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userService.loadUserModelByUserId(userId));
        return "/users/updateForm";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('Admin')")
    public String updateUser(@ModelAttribute("user") UserModel userModel, Model model, RedirectAttributes redirectAttribute) {
        try{
            Set<Role> roles = new HashSet<>();
            if(userModel.getAdmin())
                roles.add(Role.ADMIN);
            if(userModel.getOrganiser())
                roles.add(Role.ORGANISER);
            if(userModel.getAlumni())
                roles.add(Role.ALUMNI);

            User user = userService.updateUserByAdmin(userModel.getEmail(), roles);
            redirectAttribute.addFlashAttribute("message", "Successfully updated the User with Id: " + user.getUserId());
        } catch(InvalidRoleChangeException e) {
            redirectAttribute.addFlashAttribute("warning", "Failed to assign new roles to User with Id: " + userModel.getUserId() + " due to "+e.getMessage());
        } catch(Exception e) {
            redirectAttribute.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }
}
