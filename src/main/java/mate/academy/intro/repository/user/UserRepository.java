package mate.academy.intro.repository.user;

import java.util.Optional;
import mate.academy.intro.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserByEmail(String email);
}
