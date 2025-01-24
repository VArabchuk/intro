package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.model.Role;
import mate.academy.intro.repository.role.RolesRepository;
import mate.academy.intro.service.RolesService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {
    private final RolesRepository repository;

    @Override
    public Role save(Role role) {
        return repository.save(role);
    }
}
