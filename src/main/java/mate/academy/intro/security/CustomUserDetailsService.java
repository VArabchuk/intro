package mate.academy.intro.security;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(()
                -> new EntityNotFoundException("Can't find user by email " + email));
    }
}
