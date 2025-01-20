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
    public String welcome() {
        return "redirect:/admin/all";
    }

    @GetMapping("/all")
    public String allUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "allUsersPage";
    }

//    @GetMapping("/add")
//    public String addUser(Model model) {
//        User user = new User();
//        model.addAttribute("user", user);
//        return "addUser";
//    }

    @PostMapping("/add")
    public String postAddUser(@ModelAttribute("user") User user,
                              @RequestParam(required = false) String roleAdmin,
                              @RequestParam(required = false) String roleVIP) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ROLE_USER"));
        if (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        if (roleVIP != null && roleVIP.equals("ROLE_VIP")) {
            roles.add(roleService.getRoleByName("ROLE_VIP"));
        }
        user.setRoles(roles);
        userService.addUser(user);

        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(ModelMap model, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.equals(roleService.getRoleByName("ROLE_ADMIN"))) {
                model.addAttribute("roleAdmin", true);
            }
            if (role.equals(roleService.getRoleByName("ROLE_VIP"))) {
                model.addAttribute("roleVIP", true);
            }
        }
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/edit")
    public String postEditUser(@ModelAttribute("user") User user,
                               @RequestParam(required = false) String roleAdmin,
                               @RequestParam(required = false) String roleVIP,
                               @PathVariable(required = false) Long id) {

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ROLE_USER"));
        if (roleAdmin != null && roleAdmin.equals("ROLE_ADMIN")) {
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        }
        if (roleVIP != null && roleVIP.equals("ROLE_VIP")) {
            roles.add(roleService.getRoleByName("ROLE_VIP"));
        }
        user.setRoles(roles);
        userService.editUser(user);
        return "redirect:/admin";
    }


    @PostMapping("/delete")
    public String deleteUserById(@ModelAttribute("user") User user, @PathVariable(required = false) Long id) {
        System.out.println("Вызван метод удаления с ID: " + id);
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }
}
