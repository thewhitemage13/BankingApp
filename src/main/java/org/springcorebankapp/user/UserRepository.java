package org.springcorebankapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing and manipulating user data in the database.
 * <p>
 * This interface extends the {@link JpaRepository} interface, providing CRUD operations
 * for the {@link User} entity, as well as custom query methods for user-specific functionality.
 * </p>
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their login.
     * <p>
     * This method retrieves a user from the database based on the login provided.
     * The login is assumed to be unique across all users.
     * </p>
     *
     * @param login the login of the user to be found
     * @return an {@link Optional} containing the user if found, or an empty {@link Optional} if not
     */
    Optional<User> findByLogin(String login);

    /**
     * Checks if a user with the given login exists in the database.
     * <p>
     * This method performs a check to determine if a user with the specified login already exists.
     * </p>
     *
     * @param login the login to check for existence
     * @return {@code true} if a user with the given login exists, {@code false} otherwise
     */
    boolean existsUserByLogin(String login);
}
