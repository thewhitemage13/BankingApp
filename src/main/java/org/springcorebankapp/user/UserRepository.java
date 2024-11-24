package org.springcorebankapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);
}
