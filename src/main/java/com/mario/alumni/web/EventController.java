package com.mario.alumni.web;

import com.mario.alumni.app.entity.Alumni;
import com.mario.alumni.app.entity.Event;
import com.mario.alumni.app.entity.User;
import com.mario.alumni.app.enums.Role;
import com.mario.alumni.app.exception.InvalidRoleChangeException;
import com.mario.alumni.app.model.AlumniModel;
import com.mario.alumni.app.model.EventModel;
import com.mario.alumni.app.model.UserModel;
import com.mario.alumni.app.service.EventService;
import com.mario.alumni.app.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/events")
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }


    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN', 'ROLE_ORGANISER')")
    public String events(Model model) {
        List<EventModel> events = eventService.fetchAll();
        model.addAttribute("events", events);
        return "events/index";
    }

    @GetMapping(value = "upcoming")
    @PreAuthorize("hasAuthority('ROLE_ALUMNI')")
    public String upComingEvents(Model model, Authentication authentication) {
        List<EventModel> events = eventService.fetchAll().stream()
                .filter(event -> event.getEventDate().isAfter(LocalDate.now()))
                .filter(event -> event.getAlumni().stream().map(AlumniModel::getEmail).noneMatch(email -> authentication.getName().equals(email)))
                .collect(Collectors.toList());
        UUID alumniId = userService.loadUserByEmail(authentication.getName()).getAlumni().getAlumniId();
        model.addAttribute("events", events);
        model.addAttribute("page", "upcoming");
        model.addAttribute("alumniId", alumniId);
        return "events/index";
    }

    @GetMapping(value = "registered")
    @PreAuthorize("hasAuthority('ROLE_ALUMNI')")
    public String registeredEvents(Model model, Authentication authentication) {
        List<EventModel> events =
                eventService.fetchAll().stream()
                .filter(event -> event.getAlumni().stream().map(AlumniModel::getEmail).anyMatch(email -> authentication.getName().equals(email))).collect(Collectors.toList());
        model.addAttribute("events", events);
        return "events/index";
    }

    @GetMapping(value = "/delete/{eventId}")
    @PreAuthorize("hasAuthority('Admin')")
    public String deleteEvent(@PathVariable("eventId") UUID eventId) {
        eventService.removeEvent(eventId);
        return "redirect:/events";
    }

    @GetMapping("createForm")
    @PreAuthorize("hasAuthority('Admin')")
    public String createForm( Model model) {
        model.addAttribute("event", new EventModel());
        return "events/createForm";
    }

    @PostMapping("create")
    @PreAuthorize("hasAuthority('Admin')")
    public String createEvent(@ModelAttribute("event") EventModel eventModel, Model model, RedirectAttributes redirectAttribute) {
        try{
            eventModel = eventService.createEvent(eventModel);
            redirectAttribute.addFlashAttribute("message", "Successfully created the Event with Id: " + eventModel.getEventId());
        } catch(Exception e) {
            redirectAttribute.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events";
    }

    @GetMapping("updateForm/{eventId}")
    @PreAuthorize("hasAuthority('Admin')")
    public String updateForm( @PathVariable("eventId") UUID eventId, Model model) {
        model.addAttribute("event", eventService.loadEventModelById(eventId));
        return "events/updateForm";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateUser(@ModelAttribute("event") EventModel eventModel, Model model, RedirectAttributes redirectAttribute) {
        try{
            eventModel = eventService.updateEvent(eventModel);
            redirectAttribute.addFlashAttribute("message", "Successfully updated the Event with Id: " + eventModel.getEventId());
        } catch(Exception e) {
            redirectAttribute.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/events";
    }

    @GetMapping("{alumniId}/join/{eventId}")
    @PreAuthorize("hasAuthority('ROLE_ALUMNI')")
    public String joinEvent( @PathVariable("eventId") UUID eventId, @PathVariable("alumniId") UUID alumniId, Model model, RedirectAttributes redirectAttribute) {
        eventService.joinEvent(eventId, alumniId);
        redirectAttribute.addFlashAttribute("message", "Successfully registered to Event with id: " + eventId);
        return "redirect:/events/registered";
    }

    @GetMapping("{eventId}/alumni")
    @PreAuthorize("hasAuthority('ROLE_ALUMNI', 'ROLE_ORGANISER')")
    public String listAlumnisJoinedForEvent( @PathVariable("eventId") UUID eventId, Model model) {
        List<AlumniModel> alumni =  eventService.loadAlumniByEventID(eventId);
        model.addAttribute("alumni", alumni);
        model.addAttribute("eventId", eventId);
        return "events/registered-alumni-table";
    }

    @GetMapping("{eventId}/unregister/{alumniId}")
    @PreAuthorize("hasAuthority('ROLE_ORGANISER')")
    public String unregisterAlumni( @PathVariable("eventId") UUID eventId, @PathVariable("alumniId") UUID alumniId, Model model, RedirectAttributes redirectAttribute) {
        eventService.deRegisterAlumniFromEvent(eventId, alumniId);
        redirectAttribute.addFlashAttribute("message", "Successfully deregistered to Event with id: " + eventId);
        return "redirect:/events";
    }

}
