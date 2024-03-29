package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RoleService roleService;
    private final UserServiceImpl userService;

    public AdminController(RoleService roleService, UserServiceImpl userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public String getUsersList(@AuthenticationPrincipal User user, Model model) {
        List<User> list = userService.usersList();
        model.addAttribute("usersList", list);
        model.addAttribute("newUser", new User());
        model.addAttribute("newRole", new Role());
        model.addAttribute("authUser", user);
        model.addAttribute("allUsers", list);
        return "users";
    }


    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("editUser", user);
        userService.update(user, (List<Role>) user.getRoles());
        return "redirect:/admin";
    }


    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user,
                          @ModelAttribute("newRole") Role role) {
        userService.add(user);
        return "redirect:/admin";
    }

    @PostMapping ("/save")
    public String save(User user, @ModelAttribute("newRole") Role role){
        userService.update(user, user.getRoles());
        return "redirect:/admin";
    }

    @PostMapping("/remove")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping ("admin/get")
    public User get(long id) {
        return userService.getUser(id);
    }
}