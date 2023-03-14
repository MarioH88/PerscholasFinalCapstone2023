package com.mario.alumni.web;

import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.model.AlumniModel;
import com.mario.alumni.app.service.AlumniService;
import com.mario.alumni.app.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/alumni")
public class AlumniController {

    private final AlumniService alumniService;
    private final UserService userService;

    public AlumniController(AlumniService alumniService, UserService userService) {
        this.alumniService = alumniService;
        this.userService = userService;
    }

    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('Admin')")
    public String alumni(Model model) {
        Set<AlumniModel> alumni = alumniService.fetchAlumni();
        model.addAttribute("alumni", alumni);
        return "alumni/index";
    }

    @GetMapping("updateForm/{alumniId}")
    @PreAuthorize("hasAuthority('Admin')")
    public String updateForm(@PathVariable("alumniId") UUID alumniId, Model model) {
        model.addAttribute("alumni", alumniService.loadAlumniById(alumniId));
        return "alumni/updateForm";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('Admin')")
    public String updateAlumni(@ModelAttribute("alumni") AlumniModel alumniModel, Model model, RedirectAttributes redirectAttribute) {
        try {
            AlumniModel alumni = alumniService.updateAlumni(alumniModel);
            redirectAttribute.addFlashAttribute("message", "Successfully updated the alumni with Id: " + alumni.getAlumniId());
        } catch (Exception e) {
            redirectAttribute.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/alumni";
    }

    @GetMapping("delete/{alumniId}")
    @PreAuthorize("hasAuthority('Admin')")
    public String deleteAlumni(@PathVariable("alumniId") UUID alumniId, Model model, RedirectAttributes redirectAttributes) {
        alumniService.deleteAlumniById(alumniId);
        redirectAttributes.addFlashAttribute("message", "Successfully deleted the alumni with Id: " + alumniId);
        return "redirect:/alumni";
    }

//    @GetMapping("events/registered")
//    @PreAuthorize("hasAuthority('ROLE_ALUMNI')")
//    public String registeredEvents(Model model) {
//        alumniService.deleteAlumniById(alumniId);
//        redirectAttributes.addFlashAttribute("message", "Successfully deleted the alumni with Id: " + alumniId);
//        return "redirect:/alumni";
//    }
}




