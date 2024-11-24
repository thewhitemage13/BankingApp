package org.springcorebankapp.account;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<List<Account>> findByUserId(int userId);
}
