package mate.academy.intro.repository.role;

import mate.academy.intro.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Role, Long> {
    Role findRoleByRoleName(Role.RoleName roleName);
}
