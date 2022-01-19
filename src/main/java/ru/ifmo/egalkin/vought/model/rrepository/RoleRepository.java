package ru.ifmo.egalkin.vought.model.rrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.egalkin.vought.model.Role;

/**
 * Created by egalkin
 * Date: 12.10.2021
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
