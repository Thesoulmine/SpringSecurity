package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;


@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserServiceImpl userService;

    private final RoleServiceImpl roleService;

    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "")
    public String showUserTable(ModelMap model) {
        model.addAttribute("users", userService.getUsersList());
        return "admin";
    }

    @GetMapping(value = "/addUser")
    public String showAddUserForm(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRolesList());
        return "addUser";
    }

    @PostMapping(value = "/addUser")
    public String saveUser(@ModelAttribute("user") @Validated User user, BindingResult bindingResult, ModelMap model) {
        boolean hasUser = userService.getUserByUsername(user.getUsername()) != null;

        if (hasUser) {
            bindingResult.rejectValue("username", "user.username","An account already exists for this email.");
        }

        if (bindingResult.hasErrors() || hasUser) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRolesList());
            return "addUser";
        }

        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/editUser")
    public String showEditPage(ModelMap model, @RequestParam long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getRolesList());
        return "editUser";
    }

    @PutMapping(value = "/editUser")
    public String editUser(@ModelAttribute("user") @Validated User user, BindingResult bindingResult, ModelMap model) {
        User dbUser = userService.getUserByUsername(user.getUsername());
        boolean hasUser = dbUser != null;
        boolean isSameUsers = user.getId().equals(dbUser != null ? dbUser.getId() : null);

        if (hasUser && !isSameUsers) {
            bindingResult.rejectValue("username", "user.username","An account already exists for this email.");
        }

        if (bindingResult.hasErrors() || (hasUser && !isSameUsers)) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRolesList());
            return "editUser";
        }

        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "/deleteUser")
    public String deleteUser(@ModelAttribute("user") User user) {

        userService.deleteUser(user);
        return "redirect:/admin";
    }
}
