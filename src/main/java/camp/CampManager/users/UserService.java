package camp.CampManager.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void saveRole(Role role){
        roleRepository.save(role);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public void addRoleToUser(User user, Role role){
        userRepository.findByUsername(user.getUsername());
        var roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }
}
