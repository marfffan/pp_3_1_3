package app.service;


import app.model.Role;
import app.model.User;

import java.util.List;

public interface RoleService {
    Role getRoleByName(String name);

    Role getRoleById(Long id);

    List<Role> allRoles();
    public void deleteRoleFromUser(User user, Role role);

}
