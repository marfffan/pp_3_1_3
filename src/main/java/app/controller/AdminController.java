package app.controller;

import app.service.RoleService;
import app.service.UserService;
import app.model.Role;
import app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String allUsers(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userThis", userService.loadUserByUsername(principal.getName()));
        return "adminPage";
    }

    @GetMapping(value = "add")
    public String addUser(Model model, Principal principal) {
        User user = new User();
        model.addAttribute("userNew", user);
        model.addAttribute("userThis", userService.loadUserByUsername(principal.getName()));
        return "addUser";
    }

    @PostMapping(value = "add")
    public String postAddUser(@ModelAttribute("user") User user,
                              @RequestParam(required = false) String roleAdmin,
                              @RequestParam(required = false) String roleVIP) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("USER"));
        if (roleAdmin != null && roleAdmin.equals("ADMIN")) {
            roles.add(roleService.getRoleByName("ADMIN"));
        }
        if (roleVIP != null && roleVIP.equals("VIP")) {
            roles.add(roleService.getRoleByName("VIP"));
        }
        user.setRoles(roles);
        userService.addUser(user);

        return "redirect:/admin";
    }


    @PostMapping(value = {"/edit"})
    public String processUserAction(@ModelAttribute("user") User user,
                                    @RequestParam(required = false) String roleAdmin,
                                    @RequestParam(required = false) String roleVIP,
                                    @PathVariable(required = false) Long id) {

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("USER"));

        if (roleAdmin != null && roleAdmin.equals("ADMIN")) {
            roles.add(roleService.getRoleByName("ADMIN"));
        }
        if (roleVIP != null && roleVIP.equals("VIP")) {
            roles.add(roleService.getRoleByName("VIP"));
        }

        user.setRoles(roles);
        
        // Редактирование пользователя
        userService.editUser(user);
        System.out.println("Вызван метод редактирования без ID: но ID пользователя такое " + user.getId());

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUserById(@ModelAttribute("user") User user, @PathVariable(required = false) Long id) {
        System.out.println("Вызван метод удаления с ID: " + id);
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }


}

