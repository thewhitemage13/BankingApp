package org.springcorebankapp.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Account} entities.
 * <p>
 * This interface extends Spring Data JPA's {@link JpaRepository} to provide CRUD
 * operations and custom query methods for the {@code Account} entity.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Supports standard CRUD operations for {@code Account} entities.</li>
 *     <li>Provides a custom method to find accounts by a specific user ID.</li>
 * </ul>
 *
 * <p>
 * This interface uses Spring Data JPA to simplify interaction with the database.
 * Custom query methods are automatically implemented by Spring at runtime based on method names.
 * </p>
 *
 * @see JpaRepository
 * @see Account
 * @see org.springframework.stereotype.Repository
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Finds all accounts associated with a specific user ID.
     * <p>
     * This method retrieves a list of {@code Account} entities that belong to the user
     * with the specified ID. The result is wrapped in an {@link Optional} to handle cases
     * where no accounts are found for the given user ID.
     * </p>
     *
     * @param userId the ID of the user whose accounts are to be retrieved
     * @return an {@link Optional} containing a list of accounts if found,
     * or an empty {@link Optional} if no accounts are associated with the given user ID
     */
    Optional<List<Account>> findByUserId(int userId);
}
