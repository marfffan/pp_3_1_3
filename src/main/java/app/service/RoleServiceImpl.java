package app.service;

import app.model.Role;
import app.model.User;
import app.repository.RoleRepo;
import app.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepo roleRepo;
    private UserRepo userRepo;

    @Autowired
    public void setRoleRepo(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleRepo.getRoleByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        return roleRepo.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> allRoles() {
        return roleRepo.findAll();
    }

    @Transactional
    public void deleteRoleFromUser(User user, Role role) {
        user.getRoles().remove(role);
        userRepo.save(user);
        // Удаляем роль из базы данных
        roleRepo.delete(role);
    }
}
